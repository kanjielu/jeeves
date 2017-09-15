package com.cherry.jeeves;

import com.cherry.jeeves.domain.shared.FriendInvitationContent;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;
import com.cherry.jeeves.service.MessageHandler;
import com.cherry.jeeves.service.WechatHttpService;
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
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
    @Autowired
    private WechatHttpService wechatHttpService;

    @Override
    public void handleChatRoomMessage(Message message) {

    }

    @Override
    public void handlePrivateMessage(Message message) throws IOException {
        replyMessage(message);
    }

    @Override
    public boolean handleFriendInvitation(RecommendInfo info) throws IOException {
        return true;
    }

    @Override
    public void postAcceptFriendInvitation(Message message) throws IOException {
        //将该用户的微信号设置成他的昵称
        String content = StringEscapeUtils.unescapeXml(message.getContent());
        ObjectMapper xmlMapper = new XmlMapper();
        FriendInvitationContent friendInvitationContent = xmlMapper.readValue(content, FriendInvitationContent.class);
        wechatHttpService.setAlias(message.getRecommendInfo().getUserName(), friendInvitationContent.getFromusername());
    }

    private void replyMessage(Message message) throws IOException {
        wechatHttpService.sendTextMsg(message.getFromUserName(), WechatUtils.textDecode(message.getContent()));
    }
}
