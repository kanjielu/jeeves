package com.cherry.jeeves.enums;

public enum MMSound {
    OPEN(1),
    CLOSE(0);

    private final int code;

    MMSound(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
