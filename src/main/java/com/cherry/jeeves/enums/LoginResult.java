package com.cherry.jeeves.enums;

public enum  LoginResult {

    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 在手机上点击确认
     */
    AWAIT_CONFIRMATION(201),
    /**
     * 等待扫描二维码
     */
    AWAIT_SCANNING(408);

    private int code;

    LoginResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
