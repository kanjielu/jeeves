package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

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
        throw new NotImplementedException();
    }

    public SendMsgResponse sendTextMsg(String content, String toUserName) throws IOException, RestClientException {
        return wechatHttpServiceInternal.sendTextMsg(cacheService.getHostUrl(), cacheService.getBaseRequest(), content, cacheService.getOwner().getUserName(), toUserName);
    }

    public void sendImageMsg() throws RestClientException {
        wechatHttpServiceInternal.sendImageMsg();
    }

    public OpLogResponse setAlias(String newAlias, String userName) throws IOException, RestClientException {
        return wechatHttpServiceInternal.setAlias(cacheService.getHostUrl(), cacheService.getBaseRequest(), newAlias, userName);
    }

    public BatchGetContactResponse batchGetContact(ChatRoomDescription[] list) throws IOException, RestClientException {
        return wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), list);
    }

    public createChatRoomResponse createChatRoom(String[] usernames, String topic) throws IOException {
        return wechatHttpServiceInternal.createChatRoom(cacheService.getHostUrl(), cacheService.getBaseRequest(), usernames, topic);
    }
}
