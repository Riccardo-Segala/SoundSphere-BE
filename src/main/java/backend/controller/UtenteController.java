package backend.controller;

import backend.model.Utente;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController extends GenericController<Utente, UUID> {
}