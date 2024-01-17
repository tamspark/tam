package com.sparklab.TAM.exceptions;

public class LogoutFailedException extends RuntimeException {

    public LogoutFailedException(String msg) {
        super(msg);
    }

    public LogoutFailedException(String msg, Throwable t) {
        super(msg, t);
    }
}
