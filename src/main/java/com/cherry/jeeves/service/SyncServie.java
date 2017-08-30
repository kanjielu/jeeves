package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.VerifyUser;
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
        logger.info(String.format("[SYNCCHECK] retcode = %s, selector = %s", retCode, selector));
        if (retCode == RetCode.NORMAL.getCode()) {
            if (selector == Selector.NEW_MESSAGE.getCode()) {
                SyncResponse syncResponse = sync();
                onChatRoomMessageReceived(syncResponse.getAddMsgList());
                onPrivateMessageReceived(syncResponse.getAddMsgList());
                onFriendInvitationRecevied(syncResponse.getAddMsgList());
            } else if (selector == Selector.ENTER_LEAVE_CHAT.getCode()) {
                sync();
            } else if (selector == Selector.CONTACT_UPDATED.getCode()) {
                sync();
            } else if (selector == Selector.NORMAL.getCode()) {
                //Do nothing
            } else {
                throw new WechatException("syncCheckResponse ret = " + retCode);
            }
        } else {
            throw new WechatException("syncCheckResponse selector = " + selector);
        }
    }

    private SyncResponse sync() throws IOException {
        SyncResponse syncResponse = wechatHttpService.sync(cacheService.getHostUrl(), cacheService.getSyncKey(), cacheService.getBaseRequest(), cacheService.getPassTicket());
        if (!WechatUtils.checkBaseResponse(syncResponse.getBaseResponse())) {
            throw new WechatException("syncResponse ret = " + syncResponse.getBaseResponse().getRet());
        }
        cacheService.setSyncKey(syncResponse.getSyncKey());
        cacheService.setSyncCheckKey(syncResponse.getSyncCheckKey());
        logger.info("[SYNC] addmsgcount: " + syncResponse.getAddMsgCount());
        logger.info("[SYNC] delcontactcount: " + syncResponse.getDelContactCount());
        logger.info("[SYNC] getmodchatroommembercount: " + syncResponse.getModChatRoomMemberCount());
        logger.info("[SYNC] getmodcontactcount: " + syncResponse.getModContactCount());
        return syncResponse;
    }

    private void onChatRoomMessageReceived(Message[] messages) {
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            if (message.getFromUserName() != null && message.getFromUserName().startsWith("@@") && message.getMsgType() == MessageType.TEXT.getCode()) {
                logger.info("[*] chatroom message:");
                logger.info("[*] from chatroom: " + message.getFromUserName());
                logger.info("[*] from person: " + MessageUtils.getSenderOfChatRoomTextMessage(message.getContent()));
                logger.info("[*] to: " + message.getToUserName());
                logger.info("[*] content:" + WechatUtils.textDecode(MessageUtils.getChatRoomTextMessageContent(message.getContent())));
            }
        }
    }

    private void onPrivateMessageReceived(Message[] messages) throws IOException {
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            if (message.getFromUserName() != null && message.getFromUserName().startsWith("@") && message.getMsgType() == MessageType.TEXT.getCode()) {
                logger.info("[*] private message:");
                logger.info("[*] from: " + message.getFromUserName());
                logger.info("[*] to: " + message.getToUserName());
                logger.info("[*] content:" + WechatUtils.textDecode(message.getContent()));
                sendMessageBack(message.getFromUserName(), "你好");
                setAlias(message.getFromUserName(), WechatUtils.textDecode(message.getContent()));
            }
        }
    }

    private void onFriendInvitationRecevied(Message[] messages) throws IOException, URISyntaxException {
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            if (message.getMsgType() == MessageType.FRIEND_REQUEST.getCode() && cacheService.getOwner().getUserName().equals(message.getToUserName())) {
                logger.info("[*] friend invitation message:");
                logger.info("[*] from: " + message.getFromUserName());
                logger.info("[*] to: " + message.getToUserName());
                logger.info("[*] recommendinfo content:" + WechatUtils.textDecode(message.getRecommendInfo().getContent()));
                VerifyUser user = new VerifyUser();
                user.setValue(message.getRecommendInfo().getUserName());
                user.setVerifyUserTicket(message.getRecommendInfo().getTicket());
                VerifyUserResponse verifyUserResponse = wechatHttpService.acceptFriend(
                        cacheService.getHostUrl(),
                        cacheService.getBaseRequest(),
                        cacheService.getPassTicket(),
                        new VerifyUser[]{user}
                );
                if (!WechatUtils.checkBaseResponse(verifyUserResponse.getBaseResponse())) {
                    throw new WechatException("verifyUserResponse ret = " + verifyUserResponse.getBaseResponse().getRet());
                }
                sync();
            }
        }
    }

    private void sendMessageBack(String userName, String content) throws IOException {
        SendMsgResponse response = wechatHttpService.sendTextMsg(
                cacheService.getHostUrl(),
                cacheService.getBaseRequest(),
                content,
                cacheService.getOwner().getUserName(),
                userName);
    }

    private void setAlias(String userName, String alias) throws IOException {
        OpLogResponse response = wechatHttpService.setAlias(
                cacheService.getHostUrl(),
                cacheService.getPassTicket(),
                cacheService.getBaseRequest(),
                alias,
                userName);
    }
}
