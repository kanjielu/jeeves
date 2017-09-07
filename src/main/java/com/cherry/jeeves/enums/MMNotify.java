package com.cherry.jeeves.enums;

public enum MMNotify {
    OPEN(1),
    CLOSE(0);

    private final int code;

    MMNotify(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
