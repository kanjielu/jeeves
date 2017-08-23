package com.cherry.jeeves.domain.request;

import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.shared.BaseMsg;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendMsgRequest {
    @JsonProperty
    private BaseRequest BaseRequest;
    @JsonProperty
    private BaseMsg Msg;
    @JsonProperty
    private int Scene;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public BaseMsg getMsg() {
        return Msg;
    }

    public void setMsg(BaseMsg msg) {
        Msg = msg;
    }

    public int getScene() {
        return Scene;
    }

    public void setScene(int scene) {
        Scene = scene;
    }
}