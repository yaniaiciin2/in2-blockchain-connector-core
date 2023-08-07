package es.in2.dome.blockchain.connector.integration.contextbroker.controller;


import es.in2.dome.blockchain.connector.integration.contextbroker.exception.HashLinkException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContextBrokerNotificationExceptionHandler {
    @ExceptionHandler(HashLinkException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHashLinkException(HashLinkException ex) {
        return "Error in creating hash link: " + ex.getMessage();
    }

}
