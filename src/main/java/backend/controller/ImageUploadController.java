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

import java.util.Collections;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path="/api/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageUploadController {
    private final ImageStorageService storageService;
    @PostMapping(path="/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("imageFile") MultipartFile file) {
        try {
            // Delega la logica di salvataggio al servizio
            String publicPath = storageService.store(file);

            Map<String, String> response = Collections.singletonMap("path", publicPath);

            // Se ha successo restituisce 200 OK con la stringa del path nel corpo
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            Map<String, String> errorResponse = Collections.singletonMap("error", "Errore durante l'upload: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
