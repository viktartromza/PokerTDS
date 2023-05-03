package com.tromza.pokertds.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionResolver {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({NoSuchElementException.class, UsernameNotFoundException.class})
    public ResponseEntity ExceptionHandlerNotFound(Exception e) {
        log.warn(e.getClass() + e.getMessage());
        return new ResponseEntity(e.getClass() + "\n" + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    public ResponseEntity ExceptionHandlerUnsupported(Exception e) {
        log.warn(e.getClass() + e.getMessage());
        return new ResponseEntity(e.getClass() + "\n" + e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity ExceptionHandlerSecurity(Exception e) {
        log.warn(e.getClass() + e.getMessage());
        return new ResponseEntity(e.getClass() + "\n" + e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity ExceptionHandlerSQL(Exception e) {
        log.warn(e.getClass() + e.getMessage());
        return new ResponseEntity(e.getClass() + "\n" + e.getMessage(), HttpStatus.CONFLICT);
    }
}
