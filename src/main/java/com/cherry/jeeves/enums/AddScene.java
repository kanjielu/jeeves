package com.cherry.jeeves.enums;

public enum AddScene {
    QQ(4),
    EMAIL(5),
    CONTACT(6),
    WEIXIN(7),
    GROUP(8),
    UNKNOWN(9),
    MOBILE(10),
    WEB(33);

    private final int code;

    AddScene(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
