package backend.exception;

// Questa classe rappresenta l'evento "Oops, non ho trovato quello che cercavi".
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message); // Il messaggio sarà "Filiale con ID: 99 non trovata"
    }
}