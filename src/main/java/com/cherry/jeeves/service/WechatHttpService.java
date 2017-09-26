package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.enums.StatusNotifyCode;
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

    /**
     * Log out
     *
     * @throws IOException if logout fails
     */
    public void logout() throws IOException {
        wechatHttpServiceInternal.logout(cacheService.getHostUrl(), cacheService.getsKey());
    }

    /**
     * Get all the contacts
     *
     * @return contacts
     * @throws IOException if getContact fails
     */
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

    /**
     * Send plain text to a contact (not chatroom)
     *
     * @param userName the username of the contact
     * @param content  the content of text
     * @throws IOException if sendText fails
     */
    public void sendText(String userName, String content) throws IOException {
        notifyNecessary(userName);
        SendMsgResponse response = wechatHttpServiceInternal.sendText(cacheService.getHostUrl(), cacheService.getBaseRequest(), content, cacheService.getOwner().getUserName(), userName);
        WechatUtils.checkBaseResponse(response);
    }

    /**
     * Set the alias of a contact
     *
     * @param userName the username of the contact
     * @param newAlias alias
     * @throws IOException if setAlias fails
     */
    public void setAlias(String userName, String newAlias) throws IOException {
        OpLogResponse response = wechatHttpServiceInternal.setAlias(cacheService.getHostUrl(), cacheService.getBaseRequest(), newAlias, userName);
        WechatUtils.checkBaseResponse(response);
    }

    /**
     * Get contacts in chatrooms
     *
     * @param list chatroom usernames
     * @return chatroom list
     * @throws IOException if batchGetContact fails
     */
    public Set<Contact> batchGetContact(Set<String> list) throws IOException {
        ChatRoomDescription[] descriptions =
                list.stream().map(x -> {
                    ChatRoomDescription description = new ChatRoomDescription();
                    description.setUserName(x);
                    return description;
                }).toArray(ChatRoomDescription[]::new);
        BatchGetContactResponse response = wechatHttpServiceInternal.batchGetContact(cacheService.getHostUrl(), cacheService.getBaseRequest(), descriptions);
        WechatUtils.checkBaseResponse(response);
        return response.getContactList();
    }

    /**
     * Create a chatroom with a topic.
     * In fact, a topic is usually not provided when creating the chatroom.
     *
     * @param userNames the usernames of the contacts who are invited to the chatroom.
     * @param topic     the topic(or nickname)
     * @throws IOException
     */
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

    /**
     * Delete a contact from a certain chatroom (if you're the owner!)
     *
     * @param chatRoomUserName chatroom username
     * @param userName         contact username
     * @throws IOException if remove chatroom member fails
     */
    public void deleteChatRoomMember(String chatRoomUserName, String userName) throws IOException {
        DeleteChatRoomMemberResponse response = wechatHttpServiceInternal.deleteChatRoomMember(cacheService.getHostUrl(), cacheService.getBaseRequest(), chatRoomUserName, userName);
        WechatUtils.checkBaseResponse(response);
    }

    /**
     * Invite a contact to a certain chatroom
     *
     * @param chatRoomUserName chatroom username
     * @param userName         contact username
     * @throws IOException if add chatroom member fails
     */
    public void addChatRoomMember(String chatRoomUserName, String userName) throws IOException {
        AddChatRoomMemberResponse response = wechatHttpServiceInternal.addChatRoomMember(cacheService.getHostUrl(), cacheService.getBaseRequest(), chatRoomUserName, userName);
        WechatUtils.checkBaseResponse(response);
        Contact chatRoom = cacheService.getChatRooms().stream().filter(x -> chatRoomUserName.equals(x.getUserName())).findFirst().orElse(null);
        if (chatRoom == null) {
            throw new WechatException("can't find " + chatRoomUserName);
        }
        chatRoom.getMemberList().addAll(response.getMemberList());
    }

    /**
     * download images in the conversation. Note that it's better not to download image directly. This method has included cookies in the request.
     *
     * @param url image url
     * @return image data
     */
    public byte[] downloadImage(String url) {
        return wechatHttpServiceInternal.downloadImage(url);
    }

    /**
     * notify the server that all the messages in the conversation between {@code userName} and me have been read.
     *
     * @param userName the contact with whom I need to set the messages read.
     * @throws IOException if statusNotify fails.
     */
    private void notifyNecessary(String userName) throws IOException {
        if (userName == null) {
            throw new IllegalArgumentException("userName");
        }
        Set<String> unreadContacts = cacheService.getContactNamesWithUnreadMessage();
        if (unreadContacts.contains(userName)) {
            wechatHttpServiceInternal.statusNotify(cacheService.getHostUrl(), cacheService.getBaseRequest(), userName, StatusNotifyCode.READED.getCode());
            unreadContacts.remove(userName);
        }
    }
}
