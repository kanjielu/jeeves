package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.SyncCheckResponse;
import com.cherry.jeeves.domain.response.SyncResponse;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.enums.MessageType;
import com.cherry.jeeves.enums.RetCode;
import com.cherry.jeeves.enums.Selector;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.MessageUtils;
import com.cherry.jeeves.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncServie {
    private static final Logger logger = LoggerFactory.getLogger(SyncServie.class);

    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;

    public void listen() throws IOException, URISyntaxException {
        SyncCheckResponse syncCheckResponse = wechatHttpService.syncCheck(cacheService.getSyncUrl(), cacheService.getBaseRequest(), cacheService.getSyncKey());
        int retCode = syncCheckResponse.getRetcode();
        int selector = syncCheckResponse.getSelector();
        logger.info(String.format("[*] synccheck retcode = %s, synccheck selector = %s", retCode, selector));
        if (retCode == RetCode.NORMAL.getCode()) {
            if (selector == Selector.NEW_MESSAGE.getCode()) {
                SyncResponse syncResponse = sync();
                List<Message> privateMessageList = new ArrayList<>();
                List<Message> chatRoomMessageList = new ArrayList<>();
                for (int i = 0; i < syncResponse.getAddMsgList().length; i++) {
                    Message message = syncResponse.getAddMsgList()[i];
                    if (message.getFromUserName() != null)
                        if (message.getFromUserName().startsWith("@@") && message.getMsgType() == MessageType.TEXT.getCode()) {
                            chatRoomMessageList.add(message);
                        } else if (message.getFromUserName().startsWith("@") && message.getMsgType() == MessageType.TEXT.getCode()) {
                            privateMessageList.add(message);
                        }
                }
                if (privateMessageList.size() > 0) {
                    onPrivateMessageReceived(privateMessageList);
                }
                if (chatRoomMessageList.size() > 0) {
                    onChatRoomMessageReceived(chatRoomMessageList);
                }
            } else if (selector == Selector.ENTER_LEAVE_CHAT.getCode()) {
                sync();
            }
        } else {
            throw new WechatException("syncCheckResponse ret = " + retCode);
        }
    }

    private SyncResponse sync() throws IOException {
        SyncResponse syncResponse = wechatHttpService.sync(cacheService.getHostUrl(), cacheService.getSyncKey(), cacheService.getBaseRequest(), cacheService.getPassTicket());
        if (!WechatUtils.checkBaseResponse(syncResponse.getBaseResponse())) {
            throw new WechatException("syncResponse ret = " + syncResponse.getBaseResponse().getRet());
        }
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
            logger.info("[*] content:" + WechatUtils.textDecode(MessageUtils.getChatRoomTextMessageContent(message.getContent())));
        }
    }

    private void onPrivateMessageReceived(List<Message> messages) {
        for (Message message : messages) {
            logger.info("[*] private message:");
            logger.info("[*] from: " + message.getFromUserName());
            logger.info("[*] to: " + message.getToUserName());
            logger.info("[*] content:" + WechatUtils.textDecode(message.getContent()));
        }
    }
}
