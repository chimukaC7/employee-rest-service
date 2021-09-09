package com.bytecodevelocity.exception_handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
public class EmployeeGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //handles the common exceptions
    //implementation for handling all exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse(ex.getMessage(),request.getDescription(false), new Date());
       return new ResponseEntity<Object>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //implementation for handling specific exception
    @ExceptionHandler(EmployeeNotFound.class)
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exception = new ExceptionResponse(ex.getMessage(),request.getDescription(false), new Date());
        return new ResponseEntity<Object>(exception, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse exception = new ExceptionResponse("Invalid Input",ex.getBindingResult().toString(), new Date());

        return new ResponseEntity<Object>(exception, HttpStatus.BAD_REQUEST);
    }


}
