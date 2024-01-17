package com.sparklab.TAM.exceptions;

public class DuplicateException extends RuntimeException{

    public DuplicateException(){
        super();
    }
    public DuplicateException(String message) {
        super(message);
    }

}