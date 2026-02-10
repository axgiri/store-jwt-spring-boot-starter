package tech.axgiri.jwtstore.common.exception;

public class InvalidIssuerException extends RuntimeException {
    public InvalidIssuerException(String message) {
        super(message);
    }
}
