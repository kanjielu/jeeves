package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.utils.WechatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Arrays;

@Component
public class WechatHttpService {

    @Autowired
    private WechatHttpServiceInternal wechatHttpServiceInternal;
    @Autowired
    private CacheService cacheService;

    public void logout() throws IOException, RestClientException {
        wechatHttpServiceInternal.logout(cacheService.getHostUrl(), cacheService.getsKey());
    }

    public GetContactResponse getContact(long seq) throws IOException, RestClientException {
        return wechatHttpServiceInternal.getContact(cacheService.getHostUrl(), cacheService.getsKey(), seq);
    }

    public void sendAppMsg() throws RestClientException {
        wechatHttpServiceInternal.sendAppMsg();
    }

    public void sendTextMsg(String content, String toUserName) throws IOException, RestClientException {
        SendMsgResponse response = wechatHttpServiceInternal.sendTextMsg(cacheService.getHostUrl(), cacheService.getBaseRequest(), content, cacheService.getOwner().getUserName(), toUserName);
        WechatUtils.checkBaseResponse(response);
    }

    public void sendImageMsg() throws RestClientException {
        wechatHttpServiceInternal.sendImageMsg();
    }

    public void setAlias(String newAlias, String userName) throws IOException, RestClientException {
        OpLogResponse response = wechatHttpServiceInternal.setAlias(cacheService.getHostUrl(), cacheService.getBaseRequest(), newAlias, userName);
        WechatUtils.checkBaseResponse(response);
    }

    public Contact[] batchGetContact(ChatRoomDescription[] list) throws IOException, RestClientException {
        BatchGetContactResponse response = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), list);
        WechatUtils.checkBaseResponse(response);
        return response.getContactList();
    }

    public void createChatRoom(String[] usernames, String topic) throws IOException {
        CreateChatRoomResponse response = wechatHttpServiceInternal.createChatRoom(cacheService.getHostUrl(), cacheService.getBaseRequest(), usernames, topic);
        WechatUtils.checkBaseResponse(response);
        //invoke BatchGetContact after CreateChatRoom
        ChatRoomDescription description = new ChatRoomDescription();
        description.setUserName(response.getChatRoomName());
        ChatRoomDescription[] descriptions = new ChatRoomDescription[]{description};
        BatchGetContactResponse batchGetContactResponse = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), descriptions);
        WechatUtils.checkBaseResponse(batchGetContactResponse);
        cacheService.getChatRooms().addAll(Arrays.asList(batchGetContactResponse.getContactList()));

    }

    public void deleteChatRoomMember(String chatRoomUsername, String username) throws IOException {
        DeleteChatRoomMemberResponse response = wechatHttpServiceInternal.deleteChatRoomMember(cacheService.getHostUrl(), cacheService.getBaseRequest(), chatRoomUsername, username);
        WechatUtils.checkBaseResponse(response);
    }
}
