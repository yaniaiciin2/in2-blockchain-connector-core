package es.in2.blockchainconnector.exception;

public class InvalidHashlinkComparisonException extends RuntimeException {

    public InvalidHashlinkComparisonException(String message) {
        super(message);
    }

    public InvalidHashlinkComparisonException(String message, Throwable cause) {
        super(message, cause);
    }

}
