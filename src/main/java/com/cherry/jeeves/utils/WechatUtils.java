package com.cherry.jeeves.utils;

import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.exception.WechatException;

import java.nio.charset.StandardCharsets;

public class WechatUtils {
    public static void checkBaseResponse(WechatHttpResponseBase response) {
        if (response.getBaseResponse().getRet() != 0) {
            throw new WechatException(response.getClass().getSimpleName() + " ret = " + response.getBaseResponse().getRet());
        }
    }

    public static String textDecode(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text");
        }
        return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public static boolean isIndividual(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("contact");
        }
        return contact.getUserName().startsWith("@") && !contact.getUserName().startsWith("@@") && ((contact.getVerifyFlag() & 8) == 0);
    }

    public static boolean isChatRoom(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("contact");
        }
        return contact.getUserName().startsWith("@@");
    }

    public static boolean isMediaPlatform(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("contact");
        }
        return contact.getUserName().startsWith("@") && !contact.getUserName().startsWith("@@") && ((contact.getVerifyFlag() & 8) > 0);
    }
}
