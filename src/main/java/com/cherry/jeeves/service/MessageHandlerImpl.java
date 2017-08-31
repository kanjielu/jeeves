package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.OpLogResponse;
import com.cherry.jeeves.domain.response.SendMsgResponse;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;

    @Override
    public void handleChatRoomMessage(Message message) throws IOException {

    }

    @Override
    public void handlePrivateMessage(Message message) throws IOException {
        SendMsgResponse sendMsgResponse = sendMessage(message.getFromUserName(), "hello");
        if (!WechatUtils.checkBaseResponse(sendMsgResponse.getBaseResponse())) {
            throw new WechatException("sendMsgResponse ret = " + sendMsgResponse.getBaseResponse().getRet());
        }
    }

    @Override
    public boolean handleFriendInvitation(RecommendInfo info) throws IOException {
        return true;
    }

    @Override
    public void postAcceptFriendInvitation(RecommendInfo info) throws IOException {
        UUID uuid = UUID.randomUUID();
        OpLogResponse opLogResponse = setAlias(info.getUserName(), uuid.toString());
        if (!WechatUtils.checkBaseResponse(opLogResponse.getBaseResponse())) {
            throw new WechatException("opLogResponse ret = " + opLogResponse.getBaseResponse().getRet());
        }
    }


    private SendMsgResponse sendMessage(String userName, String content) throws IOException {
        return wechatHttpService.sendTextMsg(
                cacheService.getHostUrl(),
                cacheService.getBaseRequest(),
                content,
                cacheService.getOwner().getUserName(),
                userName);
    }

    private OpLogResponse setAlias(String userName, String alias) throws IOException {
        return wechatHttpService.setAlias(
                cacheService.getHostUrl(),
                cacheService.getPassTicket(),
                cacheService.getBaseRequest(),
                alias,
                userName);
    }
}
