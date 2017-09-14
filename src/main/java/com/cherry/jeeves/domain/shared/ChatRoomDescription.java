package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoomDescription {
    @JsonProperty
    private String UserName;
    @JsonProperty
    private String ChatRoomId = "";

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        ChatRoomId = chatRoomId;
    }
}
