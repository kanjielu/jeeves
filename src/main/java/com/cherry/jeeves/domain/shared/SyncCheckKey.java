package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncCheckKey {
    private int Count;
    private SyncCheckKeyPair[] List;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public SyncCheckKeyPair[] getList() {
        return List;
    }

    public void setList(SyncCheckKeyPair[] list) {
        List = list;
    }
}
