package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.cherry.jeeves.domain.shared.ChatRoomMember;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddChatRoomMemberResponse extends WechatHttpResponseBase {
    @JsonProperty
    private int MemberCount;
    @JsonProperty
    private Set<ChatRoomMember> MemberList;

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
}
