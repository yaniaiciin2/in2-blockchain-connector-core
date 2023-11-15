package es.in2.blockchainconnector.exception;

public class BlockchainNodeSubscriptionException extends RuntimeException {

    public BlockchainNodeSubscriptionException(String message) {
        super(message);
    }

    public BlockchainNodeSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

}
