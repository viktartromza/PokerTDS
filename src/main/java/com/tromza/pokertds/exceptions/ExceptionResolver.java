package com.tromza.pokertds.exceptions;

import com.tromza.pokertds.response.Response;
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
    public ResponseEntity<Response> exceptionHandlerNotFound(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        Response responseException = new Response(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    public ResponseEntity<Response> exceptionHandlerUnsupported(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        Response responseException = new Response(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<Response> exceptionHandlerSecurity(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        Response responseException = new Response(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<Response> exceptionHandlerSQL(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        Response responseException = new Response(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InterruptedException.class})
    public ResponseEntity<Response> exceptionInterruptedException(Exception e) {
        log.warn(e.getClass() + " " + e.getMessage());
        Response responseException = new Response(e.getClass().toString() + " : " + e.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.CONFLICT);
    }
}
