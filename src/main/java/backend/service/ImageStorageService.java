package backend.service;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageStorageService {

    // Definisce il percorso fisico DENTRO il container dove salvare le immagini.
    private final Path imageStorageLocation = Paths.get("/app/data/images");

    /**
     * Valida, rinomina e salva un file immagine nel file system.
     *
     * @param file Il file caricato dall'utente.
     * @return Il percorso URL pubblico e relativo del file salvato.
     */
    public String store(MultipartFile file) {
        // 1. VALIDAZIONE: Controlla che il file non sia vuoto e che sia un'immagine.
        if (file.isEmpty()) {
            throw new RuntimeException("Impossibile salvare un file vuoto.");
        }
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png");
        if (!allowedMimeTypes.contains(file.getContentType())) {
            throw new RuntimeException("Tipo di file non valido. Sono ammessi solo JPG e PNG.");
        }

        try {
            // 2. CREAZIONE NOME UNIVOCO: Genera un nome sicuro per evitare collisioni e attacchi.
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uniqueFilename = UUID.randomUUID() + "." + extension;

            // 3. SALVATAGGIO FISICO: Scrive il file sul disco nel percorso corretto.
            Path destinationFile = this.imageStorageLocation.resolve(uniqueFilename);

            // Crea la directory di destinazione se non esiste.
            Files.createDirectories(this.imageStorageLocation);

            // Copia i dati del file nella destinazione.
            Files.copy(file.getInputStream(), destinationFile);

            // 4. RESTITUZIONE PERCORSO PUBBLICO: Ritorna il percorso che il frontend userà per l'URL.
            return "/images/" + uniqueFilename;

        } catch (IOException e) {
            // Lancia un'eccezione in caso di errore di scrittura su disco.
            throw new RuntimeException("Errore nel salvataggio del file.", e);
        }
    }
}