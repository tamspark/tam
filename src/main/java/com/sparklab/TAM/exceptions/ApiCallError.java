package com.sparklab.TAM.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ApiCallError extends RuntimeException {
    private  HttpStatus httpStatus;
    private String jsonResponseString;
    private HttpStatusCode errorCode;


    public ApiCallError(String message) {
        super(message);
    }


    public ApiCallError(HttpStatusCode errorCode, String message) {
        jsonResponseString = "{\"status\":\"" + errorCode + "\",\"message\":\"" + message + "\"}";
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return jsonResponseString;
    }


    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}