package com.blatant.api.exception;

public class UserSubscriptionNotFound extends Exception {
    public UserSubscriptionNotFound() {
    }

    public UserSubscriptionNotFound(String message) {
        super(message);
    }

    public UserSubscriptionNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public UserSubscriptionNotFound(Throwable cause) {
        super(cause);
    }

    public UserSubscriptionNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
