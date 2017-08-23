package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.shared.Token;

public class RedirectLoginResult {
    private Token token;
    private String hostUrl;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }
}
