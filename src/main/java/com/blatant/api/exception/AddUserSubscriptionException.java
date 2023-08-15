package com.blatant.api.exception;

public class AddUserSubscriptionException extends Exception{
    public AddUserSubscriptionException() {
    }

    public AddUserSubscriptionException(String message) {
        super(message);
    }

    public AddUserSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddUserSubscriptionException(Throwable cause) {
        super(cause);
    }

    public AddUserSubscriptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
