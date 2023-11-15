package es.in2.blockchainconnector.exception;

public class RequestErrorException extends RuntimeException {

    public RequestErrorException(String message) {
        super(message);
    }

    public RequestErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
