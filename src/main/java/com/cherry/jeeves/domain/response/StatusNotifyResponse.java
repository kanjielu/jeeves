package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusNotifyResponse extends WechatHttpResponseBase {
    @JsonProperty
    private String MsgID;

    public String getMsgID() {
        return MsgID;
    }

    public void setMsgID(String msgID) {
        MsgID = msgID;
    }
}
