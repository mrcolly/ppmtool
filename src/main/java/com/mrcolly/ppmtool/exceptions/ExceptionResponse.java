package com.mrcolly.ppmtool.exceptions;

import java.util.Map;

public class ExceptionResponse {

    private String message;
    private Map<String, String> errors;

    public ExceptionResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String identifier) {
        this.message = identifier;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
