package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
    @JsonProperty
    private int BitFlag;
    //TODO
    @JsonProperty
    private Object UserName;
    //TODO
    @JsonProperty
    private Object NickName;
    //TODO
    @JsonProperty
    private Object BindEmail;
    //TODO
    @JsonProperty
    private Object BindMobile;
    @JsonProperty
    private int BindUin;
    @JsonProperty
    private int Status;
    @JsonProperty
    private int Sex;
    @JsonProperty
    private int PersonalCard;
    @JsonProperty
    private String Alias;
    @JsonProperty
    private int HeadImgUpdateFlag;
    @JsonProperty
    private String HeadImgUrl;
    @JsonProperty
    private String Signature;

    public int getBitFlag() {
        return BitFlag;
    }

    public void setBitFlag(int bitFlag) {
        BitFlag = bitFlag;
    }

    public Object getUserName() {
        return UserName;
    }

    public void setUserName(Object userName) {
        UserName = userName;
    }

    public Object getNickName() {
        return NickName;
    }

    public void setNickName(Object nickName) {
        NickName = nickName;
    }

    public Object getBindEmail() {
        return BindEmail;
    }

    public void setBindEmail(Object bindEmail) {
        BindEmail = bindEmail;
    }

    public Object getBindMobile() {
        return BindMobile;
    }

    public void setBindMobile(Object bindMobile) {
        BindMobile = bindMobile;
    }

    public int getBindUin() {
        return BindUin;
    }

    public void setBindUin(int bindUin) {
        BindUin = bindUin;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public int getPersonalCard() {
        return PersonalCard;
    }

    public void setPersonalCard(int personalCard) {
        PersonalCard = personalCard;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public int getHeadImgUpdateFlag() {
        return HeadImgUpdateFlag;
    }

    public void setHeadImgUpdateFlag(int headImgUpdateFlag) {
        HeadImgUpdateFlag = headImgUpdateFlag;
    }

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }
}
