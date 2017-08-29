package com.cherry.jeeves.utils;

import org.junit.Assert;
import org.junit.Test;

public class MessageUtilsTest {
    @Test
    public void getChatRoomMessageContent() throws Exception {
        String sample = "@90715b46bed6d190d4a217e402d67f9ffb6769c1ba4af7b3bebf53f37fb939b9:<br/>11";
        Assert.assertEquals(MessageUtils.getChatRoomTextMessageContent(sample), "\r\n11");
    }

    @Test
    public void getSenderOfChatRoomMessage() throws Exception {
        String sample = "@90715b46bed6d190d4a217e402d67f9ffb6769c1ba4af7b3bebf53f37fb939b9:<br/>11";
        Assert.assertEquals(MessageUtils.getSenderOfChatRoomTextMessage(sample),
                "@90715b46bed6d190d4a217e402d67f9ffb6769c1ba4af7b3bebf53f37fb939b9");
    }
}