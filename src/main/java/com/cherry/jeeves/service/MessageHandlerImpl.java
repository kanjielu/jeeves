package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.OpLogResponse;
import com.cherry.jeeves.domain.response.SendMsgResponse;
import com.cherry.jeeves.domain.shared.FriendInvitationContent;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.WechatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageHandlerImpl extends DefaultMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;

    @Override
    public void handleChatRoomMessage(Message message) throws IOException {
        super.handleChatRoomMessage(message);
    }

    @Override
    public void handlePrivateMessage(Message message) throws IOException {
        super.handlePrivateMessage(message);
        replyMessage(message);
    }

    @Override
    public boolean handleFriendInvitation(RecommendInfo info) throws IOException {
        super.handleFriendInvitation(info);
        return true;
    }

    @Override
    public void postAcceptFriendInvitation(Message message) throws IOException {
        super.postAcceptFriendInvitation(message);
        String content = StringEscapeUtils.unescapeXml(message.getContent());
        ObjectMapper xmlMapper = new XmlMapper();
        FriendInvitationContent friendInvitationContent = xmlMapper.readValue(content, FriendInvitationContent.class);
        OpLogResponse opLogResponse = setAlias(message.getRecommendInfo().getUserName(), friendInvitationContent.getFromusername());
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

    private void replyMessage(Message message) throws IOException {
        SendMsgResponse sendMsgResponse = sendMessage(message.getFromUserName(), WechatUtils.textDecode(message.getContent()));
        if (!WechatUtils.checkBaseResponse(sendMsgResponse.getBaseResponse())) {
            throw new WechatException("sendMsgResponse ret = " + sendMsgResponse.getBaseResponse().getRet());
        }
    }
}
