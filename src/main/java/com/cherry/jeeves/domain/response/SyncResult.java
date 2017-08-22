package com.cherry.jeeves.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncResult {
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
}
