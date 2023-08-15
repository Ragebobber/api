package com.blatant.api.exception;

public class SubscriptionNotFound extends Exception{
    public SubscriptionNotFound() {
    }

    public SubscriptionNotFound(String message) {
        super(message);
    }

    public SubscriptionNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriptionNotFound(Throwable cause) {
        super(cause);
    }

    public SubscriptionNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
