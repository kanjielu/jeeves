package com.cherry.jeeves.enums;

public enum LoginCode {

    /**
     * 成功
     */
    SUCCESS("200"),
    /**
     * 在手机上点击确认
     */
    AWAIT_CONFIRMATION("201"),
    /**
     * 二维码超时
     */
    EXPIRED("400"),
    /**
     * 等待扫描二维码
     */
    AWAIT_SCANNING("408");

    private final String code;

    LoginCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
