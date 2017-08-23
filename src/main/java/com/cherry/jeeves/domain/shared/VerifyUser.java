package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyUser {
    private String Value;
    private String VerifyUserTicket;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getVerifyUserTicket() {
        return VerifyUserTicket;
    }

    public void setVerifyUserTicket(String verifyUserTicket) {
        VerifyUserTicket = verifyUserTicket;
    }
}
