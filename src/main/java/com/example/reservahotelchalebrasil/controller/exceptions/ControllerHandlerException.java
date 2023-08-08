package com.example.reservahotelchalebrasil.controller.exceptions;

import com.example.reservahotelchalebrasil.service.exception.ObjectNotFoundException;
import jakarta.servlet.ServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerHandlerException {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, ServletRequest request){
        StandardError error = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
