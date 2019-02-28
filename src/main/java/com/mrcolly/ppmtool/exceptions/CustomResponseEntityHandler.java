package com.mrcolly.ppmtool.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleExceptions(CustomException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), ex.getErrors());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
