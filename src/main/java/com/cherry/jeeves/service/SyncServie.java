package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.SyncCheckResponse;
import com.cherry.jeeves.domain.response.SyncResponse;
import com.cherry.jeeves.domain.response.VerifyUserResponse;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;
import com.cherry.jeeves.domain.shared.VerifyUser;
import com.cherry.jeeves.enums.MessageType;
import com.cherry.jeeves.enums.RetCode;
import com.cherry.jeeves.enums.Selector;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class SyncServie {
    private static final Logger logger = LoggerFactory.getLogger(SyncServie.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;
    @Autowired(required = false)
    private MessageHandler messageHandler;

    @PostConstruct
    public void setMessageHandler() {
        if (messageHandler == null) {
            this.messageHandler = new DefaultMessageHandler();
        }
    }

    public void listen() throws IOException, URISyntaxException {
        SyncCheckResponse syncCheckResponse = wechatHttpService.syncCheck(cacheService.getSyncUrl(), cacheService.getBaseRequest(), cacheService.getSyncKey());
        int retCode = syncCheckResponse.getRetcode();
        int selector = syncCheckResponse.getSelector();
        logger.info(String.format("[SYNCCHECK] retcode = %s, selector = %s", retCode, selector));
        if (retCode == RetCode.NORMAL.getCode()) {
            //有新消息
            if (selector == Selector.NEW_MESSAGE.getCode()) {
                SyncResponse syncResponse = sync();
                for (Message message : syncResponse.getAddMsgList()) {
                    //私信
                    if (message.getFromUserName() != null && message.getFromUserName().startsWith("@") && message.getMsgType() == MessageType.TEXT.getCode()) {
                        if (messageHandler != null) {
                            messageHandler.handlePrivateMessage(message);
                        }
                    }
                    //群聊
                    else if (message.getFromUserName() != null && message.getFromUserName().startsWith("@@") && message.getMsgType() == MessageType.TEXT.getCode()) {
                        if (messageHandler != null) {
                            messageHandler.handleChatRoomMessage(message);
                        }
                    }
                    //好友邀请
                    else if (message.getMsgType() == MessageType.FRIEND_REQUEST.getCode() && cacheService.getOwner().getUserName().equals(message.getToUserName())) {
                        if (messageHandler != null) {
                            if (messageHandler.handleFriendInvitation(message.getRecommendInfo())) {
                                acceptFriendInvitation(message.getRecommendInfo());
                                logger.info("[*] you've accepted the invitation");
                                messageHandler.postAcceptFriendInvitation(message);
                            } else {
                                logger.info("[*] you've declined the invitation");
                                //TODO decline invitation
                            }
                        }
                    }
                }
            } else if (selector == Selector.ENTER_LEAVE_CHAT.getCode()) {
                sync();
            } else if (selector == Selector.CONTACT_UPDATED.getCode()) {
                sync();
            } else if (selector == Selector.UNKNOWN6.getCode()) {
                sync();
            } else if (selector != Selector.NORMAL.getCode()) {
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
        //mod包含新增和修改
        if (syncResponse.getModContactCount() > 0) {
            for (Contact contact : syncResponse.getModContactList()) {
                if (WechatUtils.isIndividual(contact)) {
                    cacheService.getIndividuals().remove(contact);
                    cacheService.getIndividuals().add(contact);
                } else if (WechatUtils.isChatRoom(contact)) {
                    cacheService.getChatRooms().remove(contact);
                    cacheService.getChatRooms().add(contact);
                } else if (WechatUtils.isMediaPlatform(contact)) {
                    cacheService.getMediaPlatforms().remove(contact);
                    cacheService.getMediaPlatforms().add(contact);
                }
            }
        }
        if (syncResponse.getDelContactCount() > 0) {
            for (Contact contact : syncResponse.getDelContactList()) {
                if (WechatUtils.isIndividual(contact)) {
                    cacheService.getIndividuals().remove(contact);
                } else if (WechatUtils.isChatRoom(contact)) {
                    cacheService.getChatRooms().remove(contact);
                } else if (WechatUtils.isMediaPlatform(contact)) {
                    cacheService.getMediaPlatforms().remove(contact);
                }
            }
        }
        return syncResponse;
    }

    private void acceptFriendInvitation(RecommendInfo info) throws IOException, URISyntaxException {
        VerifyUser user = new VerifyUser();
        user.setValue(info.getUserName());
        user.setVerifyUserTicket(info.getTicket());
        VerifyUserResponse verifyUserResponse = wechatHttpService.acceptFriend(
                cacheService.getHostUrl(),
                cacheService.getBaseRequest(),
                cacheService.getPassTicket(),
                new VerifyUser[]{user}
        );
        if (!WechatUtils.checkBaseResponse(verifyUserResponse.getBaseResponse())) {
            throw new WechatException("verifyUserResponse ret = " + verifyUserResponse.getBaseResponse().getRet());
        }
    }
}
