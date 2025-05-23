package com.marketplace.elastic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private record  ValidationErrorItem(String key, String message){};

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(
                                (  e) -> {
                                  return new ValidationErrorItem(((FieldError) e).getField(), e.getDefaultMessage());
                                }
                        ).toList()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleGlobalException(NoSuchElementException ex, WebRequest request) {
        return new ResponseEntity<>(new ValidationErrorItem(HttpStatus.NOT_FOUND.toString(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}