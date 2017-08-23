package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.cherry.jeeves.domain.shared.Contact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchGetContactResponse {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private Contact[] ContactList;
    @JsonProperty
    private int Count;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

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
