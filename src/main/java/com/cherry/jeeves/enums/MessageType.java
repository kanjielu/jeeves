package com.cherry.jeeves.enums;

public enum MessageType {
    /**
     * 文本消息
     */
    TEXT(1),
    /**
     * 图片消息
     */
    IMAGE(3),
    /**
     * 语音消息
     */
    VOICE(34),
    /**
     * 好友确认消息
     */
    FRIEND_REQUEST(37),
    /**
     * 推荐好友
     */
    POSSIBLE_FRIEND(40),
    /**
     * 共享名片
     */
    SHARED_VCARD(42),
    /**
     * 视频消息
     */
    VIDEO_CALL(43),
    /**
     * 动画表情
     */
    EMOJI(47),
    /**
     * 位置消息
     */
    LOCATION(48),
    /**
     * 分享链接
     */
    LINK(49),
    /**
     * VOIP
     */
    VOIP(50),
    /**
     * 群聊初始化
     */
    INIT(51),
    /**
     * VOIPNOTIFY
     */
    VOIP_NOTIFY(52),
    /**
     * VOIPINVITE
     */
    VOIP_INVITE(53),
    /**
     * 小视频
     */
    VIDEO(62),
    /**
     * SYSNOTICE
     */
    SYS_NOTICE(9999),
    /**
     * 系统消息
     */
    SYS(10000),
    /**
     * 撤回消息
     */
    WITHDRAW(10002);

    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
