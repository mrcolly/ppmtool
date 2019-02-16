package com.mrcolly.ppmtool.exceptions;

public class ProjectIdentifierExceptionResponse {

    private String identifier;

    public ProjectIdentifierExceptionResponse(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
