package com.cherry.jeeves;

import com.cherry.jeeves.domain.shared.*;
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
import java.util.Set;

@Component
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
    @Autowired
    private WechatHttpService wechatHttpService;

    @Override
    public void onReceivingChatRoomTextMessage(Message message) {

    }

    @Override
    public void onReceivingChatRoomImageMessage(Message message, String thumbImageUrl, String fullImageUrl) {

    }

    @Override
    public void onReceivingPrivateTextMessage(Message message) throws IOException {
//        将原文回复给对方
//        replyMessage(message);
    }

    @Override
    public void onReceivingPrivateImageMessage(Message message, String thumbImageUrl, String fullImageUrl) throws IOException {
//        将图片保存在本地
//        byte[] data = wechatHttpService.downloadImage(thumbImageUrl);
//        FileOutputStream fos = new FileOutputStream("thumb.jpg");
//        fos.write(data);
//        fos.close();
    }

    @Override
    public boolean onReceivingFriendInvitation(RecommendInfo info) throws IOException {
//        默认接收所有的邀请
        return true;
    }

    @Override
    public void postAcceptFriendInvitation(Message message) throws IOException {
//        将该用户的微信号设置成他的昵称
        String content = StringEscapeUtils.unescapeXml(message.getContent());
        ObjectMapper xmlMapper = new XmlMapper();
        FriendInvitationContent friendInvitationContent = xmlMapper.readValue(content, FriendInvitationContent.class);
        wechatHttpService.setAlias(message.getRecommendInfo().getUserName(), friendInvitationContent.getFromusername());
    }

    @Override
    public void onChatRoomMembersChanged(Contact chatRoom, Set<ChatRoomMember> membersJoined, Set<ChatRoomMember> membersLeft) {

    }


    @Override
    public void onNewChatRoomsFound(Set<Contact> chatRooms) {

    }

    @Override
    public void onChatRoomsDeleted(Set<Contact> chatRooms) {

    }

    @Override
    public void onNewFriendsFound(Set<Contact> contacts) {

    }

    @Override
    public void onFriendsDeleted(Set<Contact> contacts) {

    }

    @Override
    public void onNewMediaPlatformsFound(Set<Contact> mps) {

    }

    @Override
    public void onMediaPlatformsDeleted(Set<Contact> mps) {

    }

    private void replyMessage(Message message) throws IOException {
        wechatHttpService.sendText(message.getFromUserName(), WechatUtils.textDecode(message.getContent()));
    }
}
