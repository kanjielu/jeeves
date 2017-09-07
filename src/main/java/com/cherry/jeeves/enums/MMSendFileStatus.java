package com.cherry.jeeves.enums;

public enum MMSendFileStatus {
    QUEUED(0),
    SENDING(1),
    SUCCESS(2),
    FAIL(3),
    CANCEL(4);

    private final int code;

    MMSendFileStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
