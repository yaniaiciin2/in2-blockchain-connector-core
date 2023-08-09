package es.in2.dome.blockchain.connector.integration.contextbroker.exception;

public class HashLinkException extends RuntimeException {
    public HashLinkException(String message) {
        super(message);
    }

    public HashLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
