package com.cherry.jeeves.enums;

public enum AppMessageType {

    TEXT(1),
    IMG(2),
    AUDIO(3),
    VIDEO(4),
    URL(5),
    ATTACH(6),
    OPEN(7),
    EMOJI(8),
    VOICE_REMIND(9),
    SCAN_GOOD(10),
    GOOD(13),
    EMOTION(15),
    CARD_TICKET(16),
    REALTIME_SHARE_LOCATION(17),
    TRANSFERS(2000),
    RED_ENVELOPES(2001),
    READER_TYPE(100001);

    private final int code;

    AppMessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}