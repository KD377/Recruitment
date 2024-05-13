package com.example.PromoCodes.handler;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PromoCodeNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handlePromoCodeNotFound(PromoCodeNotFoundException ex) {
        Map<String,Object> response = new HashMap<>();
        response.put("error","Not found");
        response.put("status",HttpStatus.NOT_FOUND.value());
        response.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleProductNotFoundException(ProductNotFoundException ex) {
        Map<String,Object> response = new HashMap<>();
        response.put("error","Not found");
        response.put("status",HttpStatus.NOT_FOUND.value());
        response.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String,Object> response = new HashMap<>();
        if (ex.getRootCause() != null && ex.getRootCause().getMessage().contains("CONSTRAINT_INDEX_5")) {
            response.put("error","Bad request");
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Promo code already exists. Please use a different code.");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        response.put("error","Internal server error");
        response.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message","Internal server error occurred.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        response.put("status",HttpStatus.BAD_REQUEST.value());
        response.put("error","Bad request");
        response.put("errors",errors);
        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String,String> errors = new HashMap<>();

        String fieldName = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .reduce((first, second) -> second)
                .orElse("unknown field");

        String errorMessage;
        if (ex.getTargetType().getSimpleName().equals("Currency")){
            errorMessage = String.format("Invalid value for '%s': '%s'. Expected type: %s",
                    fieldName, ex.getValue(), Arrays.toString(Currency.values()));
        }
        else {
            errorMessage = String.format("Invalid value for '%s': '%s'. Expected type: %s",
                    fieldName, ex.getValue(), ex.getTargetType().getSimpleName());
        }


        errors.put(fieldName, errorMessage);
        response.put("error","Bad request");
        response.put("errors",errors);
        response.put("status",HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(response);
    }
}
