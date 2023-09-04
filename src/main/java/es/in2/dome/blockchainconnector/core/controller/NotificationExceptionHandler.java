package es.in2.dome.blockchainconnector.core.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.dome.blockchainconnector.core.exception.HashLinkException;
import es.in2.dome.blockchainconnector.core.exception.InvalidHashlinkComparisonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class NotificationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHashLinkException(HashLinkException ex) {
        return ResponseEntity.badRequest().body("Invalid hashlink");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        return ResponseEntity.badRequest().body("JSON Processing Exception");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidHashlinkComparisonException(InvalidHashlinkComparisonException ex) {
        log.error("Invalid hash: Origin entity has is different than retrieved entity");
        return ResponseEntity.badRequest().body("Invalid Hashlink Comparison Exception");
    }

}


