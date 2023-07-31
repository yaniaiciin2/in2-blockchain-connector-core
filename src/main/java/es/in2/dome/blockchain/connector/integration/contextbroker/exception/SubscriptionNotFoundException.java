package es.in2.dome.blockchain.connector.integration.contextbroker.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
