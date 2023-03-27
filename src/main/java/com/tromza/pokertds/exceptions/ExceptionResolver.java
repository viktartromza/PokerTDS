package com.tms.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionResolver {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @ExceptionHandler(ArithmeticException.class) // вид ошибки
    public String ExceptionHandler (){
        log.warn("Exception");
        return "unsuccessfully";
    }
}
