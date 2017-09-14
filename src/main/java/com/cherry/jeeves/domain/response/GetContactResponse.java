package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.cherry.jeeves.domain.shared.Contact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetContactResponse extends WechatHttpResponseBase {
    @JsonProperty
    private int MemberCount;
    @JsonProperty
    private Contact[] MemberList;
    @JsonProperty
    private long Seq;

    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public Contact[] getMemberList() {
        return MemberList;
    }

    public void setMemberList(Contact[] memberList) {
        MemberList = memberList;
    }

    public long getSeq() {
        return Seq;
    }

    public void setSeq(long seq) {
        Seq = seq;
    }
}