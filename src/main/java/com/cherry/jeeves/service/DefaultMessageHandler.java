package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;
import com.cherry.jeeves.utils.MessageUtils;
import com.cherry.jeeves.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultMessageHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);

    @Override
    public void handleChatRoomMessage(Message message) throws IOException {
        logger.info("[*] chatroom message:");
        logger.info("[*] from chatroom: " + message.getFromUserName());
        logger.info("[*] from person: " + MessageUtils.getSenderOfChatRoomTextMessage(message.getContent()));
        logger.info("[*] to: " + message.getToUserName());
        logger.info("[*] content:" + WechatUtils.textDecode(MessageUtils.getChatRoomTextMessageContent(message.getContent())));
    }

    @Override
    public void handlePrivateMessage(Message message) throws IOException {
        logger.info("[*] private message:");
        logger.info("[*] from: " + message.getFromUserName());
        logger.info("[*] to: " + message.getToUserName());
        logger.info("[*] content:" + WechatUtils.textDecode(message.getContent()));
    }

    @Override
    public boolean handleFriendInvitation(RecommendInfo info) throws IOException {
        logger.info("[*] friend invitation message:");
        logger.info("[*] recommendinfo content:" + WechatUtils.textDecode(info.getContent()));
        return false;
    }

    @Override
    public void postAcceptFriendInvitation(Message message) throws IOException {

    }
}
