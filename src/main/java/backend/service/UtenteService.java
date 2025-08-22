package backend.service;

import backend.mapper.EmployeeMapper;
import backend.mapper.UserMapper;
import backend.model.Dipendente;
import backend.model.Utente;
import backend.repository.UtenteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UtenteService extends GenericService<Utente, UUID> {

    private final UserMapper userMapper;
    private final DipendenteService dipendenteService;
    private final UtenteRepository userRepository;

    public UtenteService(UtenteRepository repository, UserMapper userMapper, DipendenteService dipendenteService, UtenteRepository userRepository) {
        super(repository); // Passa il repository al costruttore della classe base
        this.userMapper = userMapper;
        this.dipendenteService = dipendenteService;
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true) // Le operazioni di lettura dovrebbero essere transazionali
    public Object getUserDetailsById(UUID userId, boolean isDipendente) {
        if (isDipendente) {
            return dipendenteService.getEmployeeDetailsById(userId);
        } else {
            Utente utente = userRepository.findUtenteByIdWithDetails(userId)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));
            return userMapper.toDto(utente);
        }
    }
}