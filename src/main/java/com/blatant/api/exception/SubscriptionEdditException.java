package com.blatant.api.exception;

public class SubscriptionEdditException  extends Exception{
    public SubscriptionEdditException() {
    }

    public SubscriptionEdditException(String message) {
        super(message);
    }

    public SubscriptionEdditException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriptionEdditException(Throwable cause) {
        super(cause);
    }

    public SubscriptionEdditException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
