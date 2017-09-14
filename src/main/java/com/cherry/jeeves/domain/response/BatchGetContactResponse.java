package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.cherry.jeeves.domain.shared.Contact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchGetContactResponse extends WechatHttpResponseBase {
    @JsonProperty
    private Contact[] ContactList;
    @JsonProperty
    private int Count;

    public Contact[] getContactList() {
        return ContactList;
    }

    public void setContactList(Contact[] contactList) {
        ContactList = contactList;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
