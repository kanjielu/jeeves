package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;

import java.io.IOException;

public interface MessageHandler {
    void handleChatRoomMessage(Message message) throws IOException;

    void handlePrivateMessage(Message message) throws IOException;

    boolean handleFriendInvitation(RecommendInfo info) throws IOException;

    void postAcceptFriendInvitation(RecommendInfo info) throws IOException;
}
