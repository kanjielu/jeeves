package com.cherry.jeeves.domain.response;


import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.cherry.jeeves.domain.shared.ChatRoomMember;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class createChatRoomResponse {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private Object BlackList;
    @JsonProperty
    private String ChatRoomName;
    @JsonProperty
    private int MemberCount;
    @JsonProperty
    private ChatRoomMember[] MemberList;
    @JsonProperty
    private String PYInitial;
    @JsonProperty
    private String QuanPin;
    @JsonProperty
    private String Topic;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

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

    public ChatRoomMember[] getMemberList() {
        return MemberList;
    }

    public void setMemberList(ChatRoomMember[] memberList) {
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
