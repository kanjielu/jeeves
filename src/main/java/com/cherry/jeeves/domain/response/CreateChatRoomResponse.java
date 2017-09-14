package com.cherry.jeeves.domain.response;


import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.cherry.jeeves.domain.shared.ChatRoomMember;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateChatRoomResponse extends WechatHttpResponseBase {
    @JsonProperty
    private Object BlackList;
    @JsonProperty
    private String ChatRoomName;
    @JsonProperty
    private int MemberCount;
    @JsonProperty
    private Set<ChatRoomMember> MemberList;
    @JsonProperty
    private String PYInitial;
    @JsonProperty
    private String QuanPin;
    @JsonProperty
    private String Topic;

    public Object getBlackList() {
        return BlackList;
    }

    public void setBlackList(Object blackList) {
        BlackList = blackList;
    }

    public String getChatRoomName() {
        return ChatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        ChatRoomName = chatRoomName;
    }

    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public Set<ChatRoomMember> getMemberList() {
        return MemberList;
    }

    public void setMemberList(Set<ChatRoomMember> memberList) {
        MemberList = memberList;
    }

    public String getPYInitial() {
        return PYInitial;
    }

    public void setPYInitial(String PYInitial) {
        this.PYInitial = PYInitial;
    }

    public String getQuanPin() {
        return QuanPin;
    }

    public void setQuanPin(String quanPin) {
        QuanPin = quanPin;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }
}
