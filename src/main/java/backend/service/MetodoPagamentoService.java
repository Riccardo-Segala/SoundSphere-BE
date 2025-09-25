package backend.service;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.mapper.PaymentMethodMapper;
import backend.model.MetodoPagamento;
import backend.model.Utente;
import backend.repository.MetodoPagamentoRepository;
import backend.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetodoPagamentoService extends GenericService<MetodoPagamento, UUID> {
    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private UtenteRepository utenteRepository;

    public MetodoPagamentoService(MetodoPagamentoRepository repository) {
        super(repository);
    }

    public Optional<MetodoPagamento> findByIdAndUserId(UUID methodId, UUID userId) {
        return metodoPagamentoRepository.findByIdAndUtenteId(methodId, userId);
    }

    public List<ResponsePaymentMethodDTO> getAllPaymentMethodItemsByUserId(UUID userId) {
        // 1. Recupera tutti i metodi di pagamento per l'utente specificato
        List<MetodoPagamento> methodItems = metodoPagamentoRepository.findByUtente_Id(userId);

        // 2. Uso il mapper per convertire le entità in DTO
        return methodItems.stream()
                .map(paymentMethodMapper::toDto)
                .toList();
    }

    @Transactional
    public MetodoPagamento create(UUID userId, CreatePaymentMethodDTO dto) {
        // 1. Validare e recuperare l'utente
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + userId));

        // 2. Recuperare i metodi esistenti
        List<MetodoPagamento> metodiEsistenti = metodoPagamentoRepository.findByUtente_Id(userId);

        // 3. Creare il nuovo metodo e associarlo all'utente
        MetodoPagamento nuovoMetodo = paymentMethodMapper.fromCreateDto(dto);
        nuovoMetodo.setUtente(utente);

        // 4. Logica 'main'
        if (metodiEsistenti.isEmpty()) {
            // Se non ci sono metodi, questo DEVE essere il principale
            nuovoMetodo.setMain(true);
        } else if (nuovoMetodo.isMain()) {
            // Se il nuovo è 'main', retrocedi tutti gli altri
            // Non serve salvare: @Transactional si occuperà dell'update (dirty checking)
            metodiEsistenti.forEach(metodo -> metodo.setMain(false));
        }

        // 5. Salvare SOLO la nuova entità
        return metodoPagamentoRepository.save(nuovoMetodo);

    }

    @Transactional
    public MetodoPagamento update(UUID userId, UUID methodId, UpdatePaymentMethodDTO dto) {
        // 1. Il controllo di sicurezza:
        // Cerca il metodo usando sia il suo ID sia l'ID dell'utente.
        MetodoPagamento metodoEsistente = metodoPagamentoRepository.findByIdAndUtenteId(methodId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Metodo non trovato o non appartenente all'utente"));

        // 2. Applica le modifiche parziali dal DTO
        paymentMethodMapper.partialUpdateFromUpdate(dto, metodoEsistente);

        // 3. LOGICA DI BUSINESS SEMPLIFICATA
        // Se, dopo l'aggiornamento, questo metodo è diventato il principale...
        if (metodoEsistente.isMain()) {
            // ...allora ci assicuriamo che sia l'UNICO
            metodoPagamentoRepository.demoteAllOtherMainMethodsForUser(userId, methodId);
        }

        // 4. Salva l'entità che abbiamo modificato in memoria.
        MetodoPagamento savedMethod = metodoPagamentoRepository.save(metodoEsistente);

        // 5. CONTROLLO DI SICUREZZA FINALE
        // Assicurarsi che esista sempre almeno un metodo principale.
        // Questo gestisce il caso in cui l'utente retrocede l'unico metodo esistente.
        if (!metodoPagamentoRepository.existsByUtenteIdAndMain(userId, true)) {
            // Se nessun metodo è 'main', promuovi il primo che trovi (o quello appena salvato se è l'unico).
            MetodoPagamento primoMetodo = metodoPagamentoRepository.findFirstByUtente_Id(userId)
                    .orElseThrow(() -> new IllegalStateException("Nessun metodo di pagamento trovato per l'utente."));
            primoMetodo.setMain(true);
        }

        return savedMethod;
    }

    @Transactional
    public void delete(UUID userId, UUID methodId) {
        // 1. Il controllo di sicurezza:
        // Cerca il metodo usando sia il suo ID sia l'ID dell'utente.
        MetodoPagamento metodoDaCancellare = metodoPagamentoRepository.findByIdAndUtenteId(methodId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Metodo non trovato o non appartenente all'utente"));

        // 2. Se il metodo che stiamo cancellando è quello principale, gestisci la successione.
        if (metodoDaCancellare.isMain()) {

            // Cerca i possibili candidati a diventare il nuovo metodo principale.
            List<MetodoPagamento> altriMetodi = metodoPagamentoRepository.findByUtente_Id(userId)
                    .stream()
                    .filter(metodo -> !metodo.getId().equals(methodId)) // Escludi quello corrente
                    .toList();

            // Se esiste almeno un altro metodo...
            if (!altriMetodi.isEmpty()) {
                // ...promuovi il primo della lista come nuovo principale.
                MetodoPagamento nuovoMain = altriMetodi.get(0);
                nuovoMain.setMain(true);
                // L'update su 'nuovoMain' verrà gestito automaticamente da @Transactional.
            }
            // Se 'altriMetodi' è vuota, non facciamo nulla.
        }

        // 3. Esegui la cancellazione
        metodoPagamentoRepository.delete(metodoDaCancellare);
    }
}
