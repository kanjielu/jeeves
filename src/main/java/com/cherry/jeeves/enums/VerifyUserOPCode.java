package com.cherry.jeeves.enums;

public enum VerifyUserOPCode {
    ADDCONTACT(1),
    SENDREQUEST(2),
    VERIFYOK(3),
    VERIFYREJECT(4),
    SENDERREPLY(5),
    RECVERREPLY(6);

    private final int code;

    VerifyUserOPCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
