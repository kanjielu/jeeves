package com.cherry.jeeves.domain.request;

import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.shared.SyncKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncRequest {
    @JsonProperty
    private BaseRequest BaseRequest;
    @JsonProperty
    private long rr;
    @JsonProperty
    private SyncKey SyncKey;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public long getRr() {
        return rr;
    }

    public void setRr(long rr) {
        this.rr = rr;
    }

    public SyncKey getSyncKey() {
        return SyncKey;
    }

    public void setSyncKey(SyncKey syncKey) {
        SyncKey = syncKey;
    }
}
