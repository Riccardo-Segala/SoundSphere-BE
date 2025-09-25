package backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Eccezione lanciata quando un prodotto non è disponibile nella quantità richiesta
 * durante un'operazione di acquisto.
 * L'annotazione @ResponseStatus(HttpStatus.CONFLICT) dice a Spring di restituire
 * automaticamente un codice HTTP 409 Conflict quando questa eccezione non viene
 * gestita da un @ControllerAdvice.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }
}