package com.cherry.jeeves.enums;

public enum UserAttrVerifyFlag {
    BIZ(1),
    FAMOUS(2),
    BIZ_BIG(4),
    BIZ_BRAND(8),
    BIZ_VERIFIED(16);

    private final int code;

    UserAttrVerifyFlag(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
