package com.cherry.jeeves.enums;

public enum Selector {
    /**
     * 默认值
     */
    NORMAL(0),
    /**
     * 包含但不仅限于新增群
     */
    UNKNOWN1(1),
    /**
     * 新消息
     */
    NEW_MESSAGE(2),
    /**
     * 保存群聊到通讯录
     * 修改群名称
     * 新增或删除联系人
     * 群聊成员数目变化
     */
    CONTACT_UPDATED(4),
    UNKNOWN6(6),
    ENTER_LEAVE_CHAT(7);

    private final int code;

    public int getCode() {
        return code;
    }

    Selector(int code) {
        this.code = code;
    }
}
