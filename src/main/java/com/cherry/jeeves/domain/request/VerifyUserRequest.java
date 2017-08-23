package com.cherry.jeeves.domain.request;

import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.shared.VerifyUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyUserRequest {
    @JsonProperty
    private BaseRequest BaseRequest;
    @JsonProperty
    private int Opcode;
    @JsonProperty
    private int[] SceneList;
    @JsonProperty
    private int SceneListCount;
    @JsonProperty
    private String skey;
    @JsonProperty
    private String VerifyContent;
    @JsonProperty
    private VerifyUser[] VerifyUserList;
    @JsonProperty
    private int VerifyUserListSize;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public int getOpcode() {
        return Opcode;
    }

    public void setOpcode(int opcode) {
        Opcode = opcode;
    }

    public int[] getSceneList() {
        return SceneList;
    }

    public void setSceneList(int[] sceneList) {
        SceneList = sceneList;
    }

    public int getSceneListCount() {
        return SceneListCount;
    }

    public void setSceneListCount(int sceneListCount) {
        SceneListCount = sceneListCount;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getVerifyContent() {
        return VerifyContent;
    }

    public void setVerifyContent(String verifyContent) {
        VerifyContent = verifyContent;
    }

    public VerifyUser[] getVerifyUserList() {
        return VerifyUserList;
    }

    public void setVerifyUserList(VerifyUser[] verifyUserList) {
        VerifyUserList = verifyUserList;
    }

    public int getVerifyUserListSize() {
        return VerifyUserListSize;
    }

    public void setVerifyUserListSize(int verifyUserListSize) {
        VerifyUserListSize = verifyUserListSize;
    }
}
