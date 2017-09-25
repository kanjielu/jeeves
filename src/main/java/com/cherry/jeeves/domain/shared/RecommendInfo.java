package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendInfo {
    @JsonProperty
    private String UserName;
    @JsonProperty
    private String NickName;
    @JsonProperty
    private long QQNum;
    @JsonProperty
    private String Province;
    @JsonProperty
    private String City;
    @JsonProperty
    private String Content;
    @JsonProperty
    private String Signature;
    @JsonProperty
    private String Alias;
    @JsonProperty
    private int Scene;
    @JsonProperty
    private int VerifyFlag;
    @JsonProperty
    private long AttrStatus;
    @JsonProperty
    private int Sex;
    @JsonProperty
    private String Ticket;
    @JsonProperty
    private int OpCode;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public long getQQNum() {
        return QQNum;
    }

    public void setQQNum(long QQNum) {
        this.QQNum = QQNum;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public int getScene() {
        return Scene;
    }

    public void setScene(int scene) {
        Scene = scene;
    }

    public int getVerifyFlag() {
        return VerifyFlag;
    }

    public void setVerifyFlag(int verifyFlag) {
        VerifyFlag = verifyFlag;
    }

    public long getAttrStatus() {
        return AttrStatus;
    }

    public void setAttrStatus(long attrStatus) {
        AttrStatus = attrStatus;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public int getOpCode() {
        return OpCode;
    }

    public void setOpCode(int opCode) {
        OpCode = opCode;
    }
}
