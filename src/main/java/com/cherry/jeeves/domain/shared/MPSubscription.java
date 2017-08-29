package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MPSubscription {
    @JsonProperty
    private int MPArticleCount;
    @JsonProperty
    private MPArticle[] MPArticleList;
    @JsonProperty
    private String NickName;
    @JsonProperty
    private long Time;
    @JsonProperty
    private String UserName;

    public int getMPArticleCount() {
        return MPArticleCount;
    }

    public void setMPArticleCount(int MPArticleCount) {
        this.MPArticleCount = MPArticleCount;
    }

    public MPArticle[] getMPArticleList() {
        return MPArticleList;
    }

    public void setMPArticleList(MPArticle[] MPArticleList) {
        this.MPArticleList = MPArticleList;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
