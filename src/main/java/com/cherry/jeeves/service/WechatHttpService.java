package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.WechatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class WechatHttpService {

    @Autowired
    private WechatHttpServiceInternal wechatHttpServiceInternal;
    @Autowired
    private CacheService cacheService;

    public void logout() throws IOException {
        wechatHttpServiceInternal.logout(cacheService.getHostUrl(), cacheService.getsKey());
    }

    public Set<Contact> getContact() throws IOException {
        Set<Contact> contacts = new HashSet<>();
        long seq = 0;
        do {
            GetContactResponse response = wechatHttpServiceInternal.getContact(cacheService.getHostUrl(), cacheService.getsKey(), seq);
            WechatUtils.checkBaseResponse(response);
            seq = response.getSeq();
            contacts.addAll(response.getMemberList());
        }
        while (seq > 0);
        return contacts;
    }

    public void sendText(String userName, String content) throws IOException {
        SendMsgResponse response = wechatHttpServiceInternal.sendText(cacheService.getHostUrl(), cacheService.getBaseRequest(), content, cacheService.getOwner().getUserName(), userName);
        WechatUtils.checkBaseResponse(response);
    }

    public void sendImageMsg() {
        wechatHttpServiceInternal.sendImageMsg();
    }

    public void sendAppMsg() {
        wechatHttpServiceInternal.sendAppMsg();
    }

    public void setAlias(String userName, String newAlias) throws IOException {
        OpLogResponse response = wechatHttpServiceInternal.setAlias(cacheService.getHostUrl(), cacheService.getBaseRequest(), newAlias, userName);
        WechatUtils.checkBaseResponse(response);
    }

    public Set<Contact> batchGetContact(ChatRoomDescription[] list) throws IOException {
        BatchGetContactResponse response = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), list);
        WechatUtils.checkBaseResponse(response);
        return response.getContactList();
    }

    public void createChatRoom(String[] userNames, String topic) throws IOException {
        CreateChatRoomResponse response = wechatHttpServiceInternal.createChatRoom(cacheService.getHostUrl(), cacheService.getBaseRequest(), userNames, topic);
        WechatUtils.checkBaseResponse(response);
        //invoke BatchGetContact after CreateChatRoom
        ChatRoomDescription description = new ChatRoomDescription();
        description.setUserName(response.getChatRoomName());
        ChatRoomDescription[] descriptions = new ChatRoomDescription[]{description};
        BatchGetContactResponse batchGetContactResponse = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), descriptions);
        WechatUtils.checkBaseResponse(batchGetContactResponse);
        cacheService.getChatRooms().addAll(batchGetContactResponse.getContactList());
    }

    public void deleteChatRoomMember(String chatRoomUserName, String userName) throws IOException {
        DeleteChatRoomMemberResponse response = wechatHttpServiceInternal.deleteChatRoomMember(cacheService.getHostUrl(), cacheService.getBaseRequest(), chatRoomUserName, userName);
        WechatUtils.checkBaseResponse(response);
    }

    public void addChatRoomMember(String chatRoomUserName, String userName) throws IOException {
        AddChatRoomMemberResponse response = wechatHttpServiceInternal.addChatRoomMember(cacheService.getHostUrl(), cacheService.getBaseRequest(), chatRoomUserName, userName);
        WechatUtils.checkBaseResponse(response);
        Contact chatRoom = cacheService.getChatRooms().stream().filter(x -> chatRoomUserName.equals(x.getUserName())).findFirst().orElse(null);
        if (chatRoom == null) {
            throw new WechatException("can't find " + chatRoomUserName);
        }
        chatRoom.getMemberList().addAll(response.getMemberList());
    }

    public byte[] downloadImage(String url) {
        return wechatHttpServiceInternal.downloadImage(url);
    }
}
