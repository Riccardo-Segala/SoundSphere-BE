package backend.controller;

import backend.model.Utente;
import backend.model.Vantaggio;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/vantaggio")
public class VantaggioController extends GenericController<Vantaggio, UUID> {
}