package com.authentification.produit.auth.validation;

public class NotFoundExceptionHndler extends RuntimeException{

    public NotFoundExceptionHndler(String message){
        super(message);
    }
}
