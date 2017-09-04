package com.cherry.jeeves.enums;

public enum ContactFlag {

    CONTACT(1),
    CHATCONTACT(2),
    CHATROOMCONTACT(4),
    BLACKLISTCONTACT(8),
    DOMAINCONTACT(16),
    HIDECONTACT(32),
    FAVOURCONTACT(64),
    THIRDAPPCONTACT(128),
    SNSBLACKLISTCONTACT(256),
    NOTIFYCLOSECONTACT(512),
    TOPCONTACT(2048);

    private final int code;

    ContactFlag(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
