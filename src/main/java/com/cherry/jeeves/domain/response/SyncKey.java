package com.cherry.jeeves.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncKey {
    @JsonProperty
    private int Count;
    @JsonProperty
    private SyncKeyPair[] List;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public SyncKeyPair[] getList() {
        return List;
    }

    public void setList(SyncKeyPair[] list) {
        List = list;
    }
}
