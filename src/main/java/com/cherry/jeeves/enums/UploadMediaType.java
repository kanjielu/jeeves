package com.cherry.jeeves.enums;

public enum UploadMediaType {
    IMAGE(1),
    VIDEO(2),
    AUDIO(3),
    ATTACHMENT(4);

    private final int code;

    UploadMediaType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
