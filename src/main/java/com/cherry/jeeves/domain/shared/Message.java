package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @JsonProperty
    private String MsgId;
    @JsonProperty
    private String FromUserName;
    @JsonProperty
    private String ToUserName;
    @JsonProperty
    private int MsgType;
    @JsonProperty
    private String Content;
    @JsonProperty
    private long Status;
    @JsonProperty
    private long ImgStatus;
    @JsonProperty
    private long CreateTime;
    @JsonProperty
    private long VoiceLength;
    @JsonProperty
    private long PlayLength;
    @JsonProperty
    private String FileName;
    @JsonProperty
    private String FileSize;
    @JsonProperty
    private String MediaId;
    @JsonProperty
    private String Url;
    @JsonProperty
    private int AppMsgType;
    @JsonProperty
    private int StatusNotifyCode;
    @JsonProperty
    private String StatusNotifyUserName;
    @JsonProperty
    private RecommendInfo RecommendInfo;
    @JsonProperty
    private int ForwardFlag;
    @JsonProperty
    private AppInfo AppInfo;
    @JsonProperty
    private int HasProductId;
    @JsonProperty
    private String Ticket;
    @JsonProperty
    private int ImgHeight;
    @JsonProperty
    private int ImgWidth;
    @JsonProperty
    private int SubMsgType;
    @JsonProperty
    private long NewMsgId;
    @JsonProperty
    private String OriContent;

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getStatus() {
        return Status;
    }

    public void setStatus(long status) {
        Status = status;
    }

    public long getImgStatus() {
        return ImgStatus;
    }

    public void setImgStatus(long imgStatus) {
        ImgStatus = imgStatus;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public long getVoiceLength() {
        return VoiceLength;
    }

    public void setVoiceLength(long voiceLength) {
        VoiceLength = voiceLength;
    }

    public long getPlayLength() {
        return PlayLength;
    }

    public void setPlayLength(long playLength) {
        PlayLength = playLength;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getAppMsgType() {
        return AppMsgType;
    }

    public void setAppMsgType(int appMsgType) {
        AppMsgType = appMsgType;
    }

    public int getStatusNotifyCode() {
        return StatusNotifyCode;
    }

    public void setStatusNotifyCode(int statusNotifyCode) {
        StatusNotifyCode = statusNotifyCode;
    }

    public String getStatusNotifyUserName() {
        return StatusNotifyUserName;
    }

    public void setStatusNotifyUserName(String statusNotifyUserName) {
        StatusNotifyUserName = statusNotifyUserName;
    }

    public com.cherry.jeeves.domain.shared.RecommendInfo getRecommendInfo() {
        return RecommendInfo;
    }

    public void setRecommendInfo(com.cherry.jeeves.domain.shared.RecommendInfo recommendInfo) {
        RecommendInfo = recommendInfo;
    }

    public int getForwardFlag() {
        return ForwardFlag;
    }

    public void setForwardFlag(int forwardFlag) {
        ForwardFlag = forwardFlag;
    }

    public com.cherry.jeeves.domain.shared.AppInfo getAppInfo() {
        return AppInfo;
    }

    public void setAppInfo(com.cherry.jeeves.domain.shared.AppInfo appInfo) {
        AppInfo = appInfo;
    }

    public int getHasProductId() {
        return HasProductId;
    }

    public void setHasProductId(int hasProductId) {
        HasProductId = hasProductId;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public int getImgHeight() {
        return ImgHeight;
    }

    public void setImgHeight(int imgHeight) {
        ImgHeight = imgHeight;
    }

    public int getImgWidth() {
        return ImgWidth;
    }

    public void setImgWidth(int imgWidth) {
        ImgWidth = imgWidth;
    }

    public int getSubMsgType() {
        return SubMsgType;
    }

    public void setSubMsgType(int subMsgType) {
        SubMsgType = subMsgType;
    }

    public long getNewMsgId() {
        return NewMsgId;
    }

    public void setNewMsgId(long newMsgId) {
        NewMsgId = newMsgId;
    }

    public String getOriContent() {
        return OriContent;
    }

    public void setOriContent(String oriContent) {
        OriContent = oriContent;
    }
}
