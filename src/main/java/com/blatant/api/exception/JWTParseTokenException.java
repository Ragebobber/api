package com.blatant.api.exception;

public class JWTParseTokenException extends Exception{
    public JWTParseTokenException() {
    }

    public JWTParseTokenException(String message) {
        super(message);
    }

    public JWTParseTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTParseTokenException(Throwable cause) {
        super(cause);
    }

    public JWTParseTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
