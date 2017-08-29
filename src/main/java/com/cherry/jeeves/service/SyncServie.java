package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.SyncCheckResponse;
import com.cherry.jeeves.domain.response.SyncResponse;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.enums.RetCode;
import com.cherry.jeeves.enums.Selector;
import com.cherry.jeeves.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncServie {
    private static final Logger logger = LoggerFactory.getLogger(SyncServie.class);

    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;

    public void sync() throws IOException {
        SyncCheckResponse syncCheckResponse = wechatHttpService.syncCheck(cacheService.getHostUrl(), cacheService.getBaseRequest(), cacheService.getSyncKey());
        String retCode = syncCheckResponse.getRetcode();
        int selector = syncCheckResponse.getSelector();
        logger.info("[*] synccheck retcode = " + retCode);
        logger.info("[*] synccheck selector = " + selector);
        if (retCode.equals(RetCode.NORMAL.getCode())) {
            if (selector == Selector.NEW_MESSAGE.getCode()) {
                SyncResponse syncResponse = syncInternal();
                List<Message> privateMessageList = new ArrayList<>();
                List<Message> chatRoomMessageList = new ArrayList<>();
                for (int i = 0; i < syncResponse.getAddMsgList().length; i++) {
                    Message message = syncResponse.getAddMsgList()[0];
                    if (message.getFromUserName() != null)
                        if (message.getFromUserName().startsWith("@@")) {
                            chatRoomMessageList.add(message);
                        } else if (message.getFromUserName().startsWith("@")) {
                            privateMessageList.add(message);
                        }
                }
                if (privateMessageList.size() > 0) {
                    onPrivateMessageReceived(privateMessageList);
                }
                if (chatRoomMessageList.size() > 0) {
                    onChatRoomMessageReceived(chatRoomMessageList);
                }
            }
        } else {
            syncInternal();
        }
    }

    private SyncResponse syncInternal() throws IOException {
        SyncResponse syncResponse = wechatHttpService.sync(cacheService.getHostUrl(), cacheService.getSyncKey(), cacheService.getBaseRequest(), cacheService.getPassTicket());
        cacheService.setSyncKey(syncResponse.getSyncKey());
        cacheService.setSyncCheckKey(syncResponse.getSyncCheckKey());
        return syncResponse;
    }

    private void onChatRoomMessageReceived(List<Message> messages) {
        for (Message message : messages) {
            logger.info("[*] chatroom message:");
            logger.info("[*] from chatroom: " + message.getFromUserName());
            logger.info("[*] from person: " + MessageUtils.getSenderOfChatRoomTextMessage(message.getContent()));
            logger.info("[*] to: " + message.getToUserName());
            logger.info("[*] content:" + MessageUtils.getChatRoomTextMessageContent(message.getContent()));
        }
    }

    private void onPrivateMessageReceived(List<Message> messages) {
        for (Message message : messages) {
            logger.info("[*] private message:");
            logger.info("[*] from: " + message.getFromUserName());
            logger.info("[*] to: " + message.getToUserName());
            logger.info("[*] content:" + message.getContent());
        }
    }
}
