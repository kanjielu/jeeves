package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;

import java.io.IOException;

public interface MessageHandler {
    void handleChatRoomMessage(Message message);

    void handlePrivateMessage(Message message) throws IOException;

    boolean handleFriendInvitation(RecommendInfo info) throws IOException;

    void postAcceptFriendInvitation(Message message) throws IOException;
}
