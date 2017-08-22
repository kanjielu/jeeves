package com.cherry.jeeves.enums;

/**
 * 确认添加好友Enum
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年6月29日 下午9:47:14
 */
public enum VerifyFriendEnum {

    ADD(2, "添加"),
    ACCEPT(3, "接受");

    private int code;
    private String desc;

    VerifyFriendEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

}
