package com.cherry.jeeves.utils;

import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.cherry.jeeves.domain.shared.Contact;

public class WechatUtils {
    public static boolean checkBaseResponse(BaseResponse baseResponse) {
        return baseResponse.getRet() == 0;
    }

    public static String textDecode(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text");
        }
//        return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return text;
    }

    public static boolean isIndividual(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("contact");
        }
        return contact.getUserName().startsWith("@") && ((contact.getVerifyFlag() & 8) == 0);
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
        return contact.getUserName().startsWith("@") && ((contact.getVerifyFlag() & 8) > 0);
    }
}
