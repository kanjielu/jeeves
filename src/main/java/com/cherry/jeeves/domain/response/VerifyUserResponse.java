package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyUserResponse {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private String MsgID;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }
}
