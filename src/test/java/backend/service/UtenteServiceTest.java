package backend.service;

import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.mapper.UserMapper;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.Vantaggio;
import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;
import backend.repository.UtenteRepository;
import backend.mapper.resolver.RoleResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    // Mock di tutte le dipendenze del UtenteService
    @Mock
    private UtenteRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VantaggioService vantaggioService;
    @Mock
    private RuoloService ruoloService;
    @Mock
    private UtenteRuoloService utenteRuoloService;
    @Mock
    private RoleResolver roleResolver;

    // L'oggetto da testare
    @InjectMocks
    private UtenteService utenteService;


    @Test
    void createUser_conDatiValidiSenzaRuoli_creaUtenteConRuoloDefault() {
        // 1. ARRANGE
        // a) Dati di input
        CreateUserFromAdminDTO dto = new CreateUserFromAdminDTO(
                "Mario", "Rossi", "mario.rossi@test.com", "password123",
                LocalDate.now(), null, null, UUID.randomUUID(), null
        );

        // b) Oggetti finti che verranno usati o restituiti dai mock
        Utente utenteMappato = new Utente(); // Utente creato dal mapper
        utenteMappato.setNome("Mario");
        utenteMappato.setCognome("Rossi");
        utenteMappato.setEmail("mario.rossi@test.com");

        Utente utenteSalvato = new Utente(); // Utente restituito dal save
        Vantaggio vantaggioFinto = new Vantaggio();
        Ruolo ruoloDefault = new Ruolo();
        ruoloDefault.setNome("UTENTE");
        // DTO di risposta finale
        ResponseUserDTO responseDto = new ResponseUserDTO(
                UUID.randomUUID(), "Mario", "Rossi", "mario.rossi@test.com",
                LocalDate.now(), Tipologia.UTENTE, null, Sesso.MASCHIO,
                LocalDate.now(), 0,null, Collections.emptySet()
        );

        // c) Configuro il comportamento dei mock
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userMapper.fromAdminCreateDto(dto)).thenReturn(utenteMappato);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(vantaggioService.findById(dto.vantaggioId())).thenReturn(Optional.of(vantaggioFinto));
        when(userRepository.save(utenteMappato)).thenReturn(utenteSalvato);
        when(ruoloService.findByName("UTENTE")).thenReturn(ruoloDefault);
        when(userMapper.toDto(utenteSalvato)).thenReturn(responseDto);

        // 2. ACT
        // Chiamata al metodo da testare
        ResponseUserDTO result = utenteService.createUser(dto);

        // 3. ASSERT & VERIFY
        // Verifico che il risultato sia quello atteso
        assertNotNull(result);
        assertEquals(responseDto, result);

        // Verifico che la password sia stata codificata e impostata
        verify(passwordEncoder).encode("password123");
        // Uso un ArgumentCaptor per "catturare" l'utente che viene salvato
        ArgumentCaptor<Utente> utenteCaptor = ArgumentCaptor.forClass(Utente.class);
        verify(userRepository).save(utenteCaptor.capture());

        // Ora ispeziono l'utente catturato per assicurarmi che i dati siano corretti
        Utente utenteCatturato = utenteCaptor.getValue();

        // Verifica dei dati che dovrebbero arrivare direttamente dal DTO
        assertEquals("Mario", utenteCatturato.getNome());
        assertEquals("Rossi", utenteCatturato.getCognome());
        assertEquals("mario.rossi@test.com", utenteCatturato.getEmail());

        // Verifica dei valori impostati dalla logica di business del service
        assertEquals("encodedPassword", utenteCatturato.getPassword());
        assertEquals(0, utenteCatturato.getPunti());
        assertEquals(Tipologia.UTENTE, utenteCatturato.getTipologia());
        assertNotNull(utenteCatturato.getDataRegistrazione()); // Verifico che la data sia stata impostata

        // Verifica delle relazioni impostate dal service
        assertNotNull(utenteCatturato.getVantaggio());
        assertEquals(vantaggioFinto, utenteCatturato.getVantaggio());

        // Verifico che sia stato cercato e assegnato il ruolo di default
        verify(ruoloService).findByName("UTENTE");
        verify(utenteRuoloService).handleRoleTransition(List.of(utenteSalvato), List.of(ruoloDefault));
    }


    @Test
    void createUser_conDatiValidiConRuoli_creaUtenteConRuoliSpecifici() {
        // 1. ARRANGE
        // Dati di input
        CreateUserFromAdminDTO dto = new CreateUserFromAdminDTO(
                "Luca", "Verdi", "luca.verdi@test.com", "password123",
                LocalDate.now(), null, null, UUID.randomUUID(), Set.of(UUID.randomUUID())
        );

        Utente utenteMappato = new Utente();
        Utente utenteSalvato = new Utente();

        ResponseUserDTO responseDtoFinto = new ResponseUserDTO(
                UUID.randomUUID(), "Luca", "Verdi", "luca.verdi@test.com",
                LocalDate.now(), Tipologia.UTENTE, null, Sesso.MASCHIO,
                LocalDate.now(), 0, null, Collections.emptySet()
        );

        // Configuro i mock per il percorso con ruoli specifici
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userMapper.fromAdminCreateDto(dto)).thenReturn(utenteMappato);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(vantaggioService.findById(any())).thenReturn(Optional.of(new Vantaggio()));
        when(userRepository.save(utenteMappato)).thenReturn(utenteSalvato);
        when(userMapper.toDto(utenteSalvato)).thenReturn(responseDtoFinto);

        // La configurazione chiave: il resolver trova i ruoli
        when(roleResolver.findRolesByIds(dto.ruoliIds())).thenReturn(Collections.emptySet());


        // 2. ACT
        // Chiamata al metodo da testare
        utenteService.createUser(dto);

        // 3. ASSERT & VERIFY
        // Verifico che sia stato chiamato il resolver
        verify(roleResolver).findRolesByIds(dto.ruoliIds());
        verify(ruoloService, never()).findByName(anyString());
    }


    @Test
    void createUser_conEmailEsistente_lanciaIllegalArgumentException() {
        // 1. ARRANGE
        CreateUserFromAdminDTO dto = new CreateUserFromAdminDTO(
                "Mario", "Rossi", "mario.rossi@test.com", "password123",
                null, null, null, null, null
        );

        // La condizione chiave: il repository dice che l'email esiste già
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        // 2. ACT & ASSERT
        // Verifico che venga lanciata l'eccezione
        assertThrows(IllegalArgumentException.class, () -> {
            utenteService.createUser(dto);
        });

        // 3. VERIFY
        // Verifico che nessuna operazione di creazione sia avvenuta
        verify(userMapper, never()).fromAdminCreateDto(any());
        verify(userRepository, never()).save(any());
    }
}