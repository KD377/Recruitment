package com.example.PromoCodes.exception;

public class PromoCodeNotFoundException extends RuntimeException{
    public PromoCodeNotFoundException(String message) {
        super(message);
    }
}
