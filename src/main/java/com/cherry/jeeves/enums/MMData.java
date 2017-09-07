package com.cherry.jeeves.enums;

public enum MMData {

    TEXT(1),
    HTML(2),
    IMG(3),
    PRIVATEMSG_TEXT(11),
    PRIVATEMSG_HTML(12),
    PRIVATEMSG_IMG(13),
    VOICEMSG(34),
    PUSHMAIL(35),
    QMSG(36),
    VERIFYMSG(37),
    PUSHSYSTEMMSG(38),
    QQLIXIANMSG_IMG(39),
    POSSIBLEFRIEND_MSG(40),
    SHARECARD(42),
    VIDEO(43),
    VIDEO_IPHONE_EXPORT(44),
    EMOJI(47),
    LOCATION(48),
    APPMSG(49),
    VOIPMSG(50),
    STATUSNOTIFY(51),
    VOIPNOTIFY(52),
    VOIPINVITE(53),
    MICROVIDEO(62),
    SYSNOTICE(9999),
    SYS(10000),
    RECALLED(10002);

    private final int code;

    MMData(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
