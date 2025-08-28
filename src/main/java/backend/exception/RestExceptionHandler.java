package backend.exception;

import backend.dto.error.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice // Dichiara questa classe come un gestore globale
public class RestExceptionHandler {

    // Questo metodo si attiva solo per le ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException ex) {

        ErrorDTO errorResponse = new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Potresti aggiungere altri @ExceptionHandler qui per gestire altri tipi di eccezioni!
    // @ExceptionHandler(MethodArgumentNotValidException.class) -> per errori di validazione 400
    // @ExceptionHandler(AccessDeniedException.class) -> per errori di permessi 403
}