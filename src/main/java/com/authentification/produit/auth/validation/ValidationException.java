package com.authentification.produit.auth.validation;

public class ValidationException extends RuntimeException {
    public ValidationException(String message){
        super(message);
    }
}
