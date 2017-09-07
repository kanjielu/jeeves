package com.cherry.jeeves.enums;

public enum MessageSendStatus {
    READY(0),
    SENDING(1),
    SUCC(2),
    FAIL(5);

    private final int code;

    MessageSendStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
