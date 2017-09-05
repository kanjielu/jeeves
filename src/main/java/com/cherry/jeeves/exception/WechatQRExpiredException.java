package com.cherry.jeeves.exception;

public class WechatQRExpiredException extends RuntimeException {
    public WechatQRExpiredException() {
    }

    public WechatQRExpiredException(String message) {
        super(message);
    }

    public WechatQRExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public WechatQRExpiredException(Throwable cause) {
        super(cause);
    }

    public WechatQRExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
