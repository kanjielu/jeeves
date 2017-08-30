package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.cherry.jeeves.domain.shared.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncResponse {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private int AddMsgCount;
    @JsonProperty
    private Message[] AddMsgList;
    @JsonProperty
    private int ModContactCount;
    @JsonProperty
    private Contact[] ModContactList;
    @JsonProperty
    private int DelContactCount;
    @JsonProperty
    private Contact[] DelContactList;
    @JsonProperty
    private int ModChatRoomMemberCount;
    @JsonProperty
    private Contact[] ModChatRoomMemberList;
    @JsonProperty
    private Profile Profile;
    @JsonProperty
    private int ContinueFlag;
    @JsonProperty
    private SyncKey SyncKey;
    @JsonProperty
    private String SKey;
    @JsonProperty
    private SyncCheckKey SyncCheckKey;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

    public int getAddMsgCount() {
        return AddMsgCount;
    }

    public void setAddMsgCount(int addMsgCount) {
        AddMsgCount = addMsgCount;
    }

    public Message[] getAddMsgList() {
        return AddMsgList;
    }

    public void setAddMsgList(Message[] addMsgList) {
        AddMsgList = addMsgList;
    }

    public int getModContactCount() {
        return ModContactCount;
    }

    public void setModContactCount(int modContactCount) {
        ModContactCount = modContactCount;
    }

    public Contact[] getModContactList() {
        return ModContactList;
    }

    public void setModContactList(Contact[] modContactList) {
        ModContactList = modContactList;
    }

    public int getDelContactCount() {
        return DelContactCount;
    }

    public void setDelContactCount(int delContactCount) {
        DelContactCount = delContactCount;
    }

    public Contact[] getDelContactList() {
        return DelContactList;
    }

    public void setDelContactList(Contact[] delContactList) {
        DelContactList = delContactList;
    }

    public int getModChatRoomMemberCount() {
        return ModChatRoomMemberCount;
    }

    public void setModChatRoomMemberCount(int modChatRoomMemberCount) {
        ModChatRoomMemberCount = modChatRoomMemberCount;
    }

    public Contact[] getModChatRoomMemberList() {
        return ModChatRoomMemberList;
    }

    public void setModChatRoomMemberList(Contact[] modChatRoomMemberList) {
        ModChatRoomMemberList = modChatRoomMemberList;
    }

    public Profile getProfile() {
        return Profile;
    }

    public void setProfile(Profile profile) {
        this.Profile = profile;
    }

    public int getContinueFlag() {
        return ContinueFlag;
    }

    public void setContinueFlag(int continueFlag) {
        ContinueFlag = continueFlag;
    }

    public SyncKey getSyncKey() {
        return SyncKey;
    }

    public void setSyncKey(SyncKey syncKey) {
        SyncKey = syncKey;
    }

    public String getSKey() {
        return SKey;
    }

    public void setSKey(String SKey) {
        this.SKey = SKey;
    }

    public SyncCheckKey getSyncCheckKey() {
        return SyncCheckKey;
    }

    public void setSyncCheckKey(SyncCheckKey syncCheckKey) {
        SyncCheckKey = syncCheckKey;
    }
}
