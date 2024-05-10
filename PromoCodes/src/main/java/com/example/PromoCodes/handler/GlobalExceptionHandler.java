package com.example.PromoCodes.handler;

import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PromoCodeNotFoundException.class)
    public ResponseEntity<String> handlePromoCodeNotFound(PromoCodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND.value() + " " + ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND.value() + " " + ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getRootCause() != null && ex.getRootCause().getMessage().contains("CONSTRAINT_INDEX_5")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Promo code already exists. Please use a different code.");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error occurred.");
    }
}
