package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.shared.ChatRoomMember;
import com.cherry.jeeves.domain.shared.Contact;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.RecommendInfo;

import java.io.IOException;
import java.util.Set;

public interface MessageHandler {
    void onReceivingChatRoomTextMessage(Message message);

    void onReceivingChatRoomImageMessage(Message message, String thumbImageUrl, String fullImageUrl);

    void onReceivingPrivateTextMessage(Message message) throws IOException;

    void onReceivingPrivateImageMessage(Message message, String thumbImageUrl, String fullImageUrl) throws IOException;

    boolean onReceivingFriendInvitation(RecommendInfo info) throws IOException;

    void postAcceptFriendInvitation(Message message) throws IOException;

    void onChatRoomMembersChanged(Contact chatRoom, Set<ChatRoomMember> membersJoined, Set<ChatRoomMember> membersLeft);

    void onNewChatRoomsFound(Set<Contact> chatRooms);

    void onChatRoomsDeleted(Set<Contact> chatRooms);

    void onNewFriendsFound(Set<Contact> contacts);

    void onFriendsDeleted(Set<Contact> contacts);

    void onNewMediaPlatformsFound(Set<Contact> mps);

    void onMediaPlatformsDeleted(Set<Contact> mps);
}
