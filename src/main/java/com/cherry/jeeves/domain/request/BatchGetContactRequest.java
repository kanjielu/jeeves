package com.cherry.jeeves.domain.request;

import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchGetContactRequest {
    @JsonProperty
    private com.cherry.jeeves.domain.request.component.BaseRequest BaseRequest;
    @JsonProperty
    private int Count;
    @JsonProperty
    private ChatRoomDescription[] List;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public ChatRoomDescription[] getList() {
        return List;
    }

    public void setList(ChatRoomDescription[] list) {
        List = list;
    }
}
