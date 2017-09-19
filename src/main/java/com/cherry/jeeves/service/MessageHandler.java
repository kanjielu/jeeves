package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;

import java.io.IOException;

public interface MessageHandler {
    void handleChatRoomTextMessage(Message message);

    void handleChatRoomImageMessage(Message message, String thumbImageUrl, String fullImageUrl);

    void handlePrivateTextMessage(Message message) throws IOException;

    void handlePrivateImageMessage(Message message, String thumbImageUrl, String fullImageUrl) throws IOException;

    boolean handleFriendInvitation(RecommendInfo info) throws IOException;

    void postAcceptFriendInvitation(Message message) throws IOException;
}
