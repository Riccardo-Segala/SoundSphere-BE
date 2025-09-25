package backend.controller;

import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.security.CustomUserDetails;
import backend.service.UtenteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path="/api/utenti", produces = MediaType.APPLICATION_JSON_VALUE)
public class UtenteController{
    private final UtenteService userService;

    public UtenteController(UtenteService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Controlla il ruolo chiamando .getAuthorities() direttamente sul principal
        boolean isDipendente = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DIPENDENTE"));

        // Prende l'ID direttamente, senza bisogno di getPrincipal() o cast.
        UUID userId = userDetails.getId();

        // chiama il service
        Object dto = userService.getUserDetailsById(userId, isDipendente);

        // restituisce la risposta
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<ResponseUserDTO> updateCurrentUser(
            @RequestBody UpdateUserDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(userService.updateCurrentUser(userId, dto));
    }
}