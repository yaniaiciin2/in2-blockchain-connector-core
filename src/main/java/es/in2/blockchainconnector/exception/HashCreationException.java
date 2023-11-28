package es.in2.blockchainconnector.exception;

public class HashCreationException extends RuntimeException {

    public HashCreationException(String message) {
        super(message);
    }

    public HashCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
