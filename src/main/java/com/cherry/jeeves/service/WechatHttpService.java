package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.exception.WechatException;
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
        if (!WechatUtils.checkBaseResponse(response.getBaseResponse())) {
            throw new WechatException("SendMsgResponse ret = " + response.getBaseResponse().getRet());
        }
    }

    public void sendImageMsg() throws RestClientException {
        wechatHttpServiceInternal.sendImageMsg();
    }

    public void setAlias(String newAlias, String userName) throws IOException, RestClientException {
        OpLogResponse response = wechatHttpServiceInternal.setAlias(cacheService.getHostUrl(), cacheService.getBaseRequest(), newAlias, userName);
        if (!WechatUtils.checkBaseResponse(response.getBaseResponse())) {
            throw new WechatException("OpLogResponse ret = " + response.getBaseResponse().getRet());
        }
    }

    public Contact[] batchGetContact(ChatRoomDescription[] list) throws IOException, RestClientException {
        BatchGetContactResponse response = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), list);
        if (!WechatUtils.checkBaseResponse(response.getBaseResponse())) {
            throw new WechatException("BatchGetContactResponse ret = " + response.getBaseResponse().getRet());
        }
        return response.getContactList();
    }

    public void createChatRoom(String[] usernames, String topic) throws IOException {
        createChatRoomResponse response = wechatHttpServiceInternal.createChatRoom(cacheService.getHostUrl(), cacheService.getBaseRequest(), usernames, topic);
        if (!WechatUtils.checkBaseResponse(response.getBaseResponse())) {
            throw new WechatException("createChatRoomResponse ret = " + response.getBaseResponse().getRet());
        }
        //invoke BatchGetContact after CreateChatRoom
        ChatRoomDescription description = new ChatRoomDescription();
        description.setUserName(response.getChatRoomName());
        ChatRoomDescription[] descriptions = new ChatRoomDescription[]{description};
        BatchGetContactResponse batchGetContactResponse = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), descriptions);
        if (!WechatUtils.checkBaseResponse(batchGetContactResponse.getBaseResponse())) {
            throw new WechatException("batchGetContactResponse ret = " + batchGetContactResponse.getBaseResponse().getRet());
        } else {
            cacheService.getChatRooms().addAll(Arrays.asList(batchGetContactResponse.getContactList()));
        }
    }
}
