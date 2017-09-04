package com.cherry.jeeves.enums;

public enum RetCode {
    NORMAL(0),
    LOGOUT1(1100),
    LOGOUT2(1101),
    LOGOUT3(1102);

    private final int code;

    RetCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
