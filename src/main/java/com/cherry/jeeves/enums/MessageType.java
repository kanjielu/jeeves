package com.cherry.jeeves.enums;

public enum MessageType {
    TEXT(1),
    IMAGE(3),
    VOICE(34),
    VIDEO(43),
    MICROVIDEO(62),
    EMOTICON(47),
    APP(49),
    VOIPMSG(50),
    VOIPNOTIFY(52),
    VOIPINVITE(53),
    LOCATION(48),
    STATUSNOTIFY(51),
    SYSNOTICE(9999),
    POSSIBLEFRIEND_MSG(40),
    VERIFYMSG(37),
    SHARECARD(42),
    SYS(10000),
    RECALLED(10002);

    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
