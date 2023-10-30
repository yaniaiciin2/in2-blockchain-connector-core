package es.in2.blockchain.connector.core.exception;

public class HashLinkException extends RuntimeException {
    public HashLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public HashLinkException(String message) {
        super(message);
    }

}
