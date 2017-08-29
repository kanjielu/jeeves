package com.cherry.jeeves.utils;

import com.cherry.jeeves.domain.response.component.BaseResponse;

import java.nio.charset.StandardCharsets;

public class WechatUtils {
    public static boolean checkBaseResponse(BaseResponse baseResponse) {
        return baseResponse.getRet() == 0;
    }

    public static String textDecode(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text");
        }
        return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
