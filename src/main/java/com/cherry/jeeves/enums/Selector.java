package com.cherry.jeeves.enums;

public enum  Selector {
    NORMAL(0),
    NEW_MESSAGE(2),
    ENTER_LEAVE_CHAT(7);

    private int code;

    Selector(int code) {
        this.code = code;
    }
}
