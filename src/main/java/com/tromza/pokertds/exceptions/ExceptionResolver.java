package com.tromza.pokertds.exceptions;

import com.sun.jdi.InternalException;
import com.tromza.pokertds.model.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionResolver {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({NoSuchElementException.class, UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<MessageResponse> exceptionHandlerNotFound(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    public ResponseEntity<MessageResponse> exceptionHandlerUnsupported(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<MessageResponse> exceptionHandlerSecurity(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<MessageResponse> exceptionHandlerSQL(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InterruptedException.class})
    public ResponseEntity<MessageResponse> exceptionInterruptedException(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InternalException.class})
    public ResponseEntity<MessageResponse> customExceptionHandler(InternalException e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<MessageResponse> exceptionHandler(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        MessageResponse messageResponseException = new MessageResponse(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(messageResponseException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
