package org.example.exceptions;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(String message){
        super(message);
    }
}
