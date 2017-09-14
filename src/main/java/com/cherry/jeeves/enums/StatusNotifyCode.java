package com.cherry.jeeves.enums;

public enum StatusNotifyCode {

    /**
     * 消息已读
     */
    READED(1),
    ENTER_SESSION(2),
    /**
     * 初始化已完成
     */
    INITED(3),
    SYNC_CONV(4),
    QUIT_SESSION(5);

    private final int code;

    StatusNotifyCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
