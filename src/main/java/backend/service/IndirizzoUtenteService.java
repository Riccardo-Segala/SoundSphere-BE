package backend.service;

import backend.dto.indirizzo_utente.CreateUserAddressDTO;
import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import backend.dto.indirizzo_utente.UpdateUserAddressDTO;
import backend.mapper.UserAddressMapper;
import backend.model.IndirizzoUtente;
import backend.model.Utente;
import backend.repository.IndirizzoUtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IndirizzoUtenteService extends GenericService<IndirizzoUtente, UUID> {
    private final IndirizzoUtenteRepository userAddressRepository;
    private final UtenteService utenteService;
    private final UserAddressMapper userAddressMapper;

    public IndirizzoUtenteService(IndirizzoUtenteRepository repository, UtenteService utenteService, UserAddressMapper userAddressMapper) {
        super(repository); // Passa il repository al costruttore della classe base
        this.userAddressRepository = repository;
        this.utenteService = utenteService;
        this.userAddressMapper = userAddressMapper;
    }

    public IndirizzoUtente findByIdAndValidateOwnership(UUID indirizzoId, UUID utenteId) {
        IndirizzoUtente indirizzo = userAddressRepository.findById(indirizzoId)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato"));

        if (!indirizzo.getUtente().getId().equals(utenteId)) {
            throw new SecurityException("L'indirizzo non appartiene all'utente corrente.");
        }
        return indirizzo;
    }

    @Transactional(readOnly = true)
    public List<ResponseUserAddressDTO> getAllUserAddressByUserId(UUID userId) {
        // Verifica se l'utente esiste
        if (!utenteService.existsById(userId)) {
            throw new EntityNotFoundException("Utente non trovato con ID: " + userId);
        }

        List<IndirizzoUtente> indirizzi = userAddressRepository.findByUtenteId(userId);
        return indirizzi.stream()
                .map(userAddressMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public ResponseUserAddressDTO findAddressById(UUID userId, UUID addressId) {
        return userAddressRepository.findByIdAndUtenteId(addressId, userId)
                .map(userAddressMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato con ID: " + addressId));
    }

    @Transactional
    public ResponseUserAddressDTO createForUser(UUID userId, CreateUserAddressDTO createDTO) {
        // 1. Trova l'utente a cui associare il nuovo indirizzo.
        Utente utente = utenteService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + userId));

        // 2. Converte il DTO in un'entità.
        IndirizzoUtente nuovoIndirizzo = userAddressMapper.fromCreateDto(createDTO);

        // 3. Collega l'indirizzo all'utente proprietario.
        nuovoIndirizzo.setUtente(utente);

        // --- 4. CONTROLLO ANTI-DUPLICATI ---
        // Creo un matcher per dire a JPA di ignorare i campi non pertinenti.
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "isDefault");

        // Creo un "esempio" usando l'indirizzo che vorrei salvare e le regole del matcher.
        Example<IndirizzoUtente> example = Example.of(nuovoIndirizzo, matcher);

        // Esegue il controllo: se un indirizzo che combacia con l'esempio esiste già, lancia un errore.
        if (userAddressRepository.exists(example)) {
            throw new DataIntegrityViolationException("Questo indirizzo esiste già per l'utente.");
        }

        // 5. Salva l'entità ora che siamo sicuri non sia un duplicato.
        IndirizzoUtente savedIndirizzo = userAddressRepository.save(nuovoIndirizzo);

        // 6. Ritorna il DTO della risorsa creata.
        return userAddressMapper.toDto(savedIndirizzo);
    }

    @Transactional
    public ResponseUserAddressDTO updateAddress(UUID userId, UUID addressId, UpdateUserAddressDTO updateDTO) {
        // 1. Il controllo di sicurezza:
        // Cerca l'indirizzo usando sia il suo ID sia l'ID dell'utente.
        // Se non viene trovato, significa o che l'indirizzo non esiste, o che non appartiene all'utente.
        // In entrambi i casi, l'operazione fallisce in modo sicuro.
        IndirizzoUtente indirizzo = userAddressRepository.findByIdAndUtenteId(addressId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato o non appartenente all'utente"));

        // 2. Se il controllo passa, applica le modifiche dall'UpdateDTO all'entità.
        userAddressMapper.partialUpdateFromUpdate(updateDTO, indirizzo);

        // 3. Salva l'entità aggiornata.
        IndirizzoUtente savedIndirizzo = userAddressRepository.save(indirizzo);

        // 4. Restituisce il DTO aggiornato.
        return userAddressMapper.toDto(savedIndirizzo);
    }

    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        // 1. Il controllo di sicurezza:
        // Cerca l'indirizzo usando sia il suo ID sia l'ID dell'utente.
        // Se non viene trovato, significa o che l'indirizzo non esiste, o che non appartiene all'utente.
        // In entrambi i casi, l'operazione fallisce in modo sicuro.
        IndirizzoUtente indirizzo = userAddressRepository.findByIdAndUtenteId(addressId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato o non appartenente all'utente"));

        // 2. Se il controllo passa, procedi con la cancellazione.
        userAddressRepository.delete(indirizzo);
    }

}
