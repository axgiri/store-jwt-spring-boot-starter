package tech.axgiri.jwtstore.exception;

public class InvalidIssuerException extends RuntimeException {
    public InvalidIssuerException(String message) {
        super(message);
    }
    
}
