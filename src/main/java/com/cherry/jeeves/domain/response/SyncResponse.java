package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.cherry.jeeves.domain.shared.Message;
import com.cherry.jeeves.domain.shared.Profile;
import com.cherry.jeeves.domain.shared.SyncCheckKey;
import com.cherry.jeeves.domain.shared.SyncKey;
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
    //TODO
    @JsonProperty
    private Object[] ModContactList;
    @JsonProperty
    private int DelContactCount;
    //TODO
    @JsonProperty
    private Object[] DelContactList;
    @JsonProperty
    private int ModChatRoomMemberCount;
    //TODO
    @JsonProperty
    private Object[] ModChatRoomMemberList;
    @JsonProperty
    private Profile profile;
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

    public Object[] getModContactList() {
        return ModContactList;
    }

    public void setModContactList(Object[] modContactList) {
        ModContactList = modContactList;
    }

    public int getDelContactCount() {
        return DelContactCount;
    }

    public void setDelContactCount(int delContactCount) {
        DelContactCount = delContactCount;
    }

    public Object[] getDelContactList() {
        return DelContactList;
    }

    public void setDelContactList(Object[] delContactList) {
        DelContactList = delContactList;
    }

    public int getModChatRoomMemberCount() {
        return ModChatRoomMemberCount;
    }

    public void setModChatRoomMemberCount(int modChatRoomMemberCount) {
        ModChatRoomMemberCount = modChatRoomMemberCount;
    }

    public Object[] getModChatRoomMemberList() {
        return ModChatRoomMemberList;
    }

    public void setModChatRoomMemberList(Object[] modChatRoomMemberList) {
        ModChatRoomMemberList = modChatRoomMemberList;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public  SyncCheckKey getSyncCheckKey() {
        return SyncCheckKey;
    }

    public void setSyncCheckKey(SyncCheckKey syncCheckKey) {
        SyncCheckKey = syncCheckKey;
    }
}
