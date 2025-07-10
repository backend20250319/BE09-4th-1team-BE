package com.playvoice.commentservice.exception;

public enum ErrorCode {
    UNAUTHORIZED("Full authentication is required to access this resource"),
    FORBIDDEN("Access is denied");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
