package backend.controller;

import backend.service.ImageStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping(path="/api/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageUploadController {
    private final ImageStorageService storageService;
    @PostMapping(path="/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("imageFile") MultipartFile file) {
        try {
            // Delega la logica di salvataggio al servizio
            String publicPath = storageService.store(file);

            // SUCCESSO: Restituisce 200 OK con la stringa del path nel corpo
            return ResponseEntity.ok(publicPath);

        } catch (RuntimeException e) {
            // ERRORE: Restituisce 500 Internal Server Error con la stringa
            // del messaggio di errore nel corpo.
            return ResponseEntity
                    .status(500)
                    .body("Errore durante l'upload: " + e.getMessage());
        }
    }
}
