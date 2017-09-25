package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "msg")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendInvitationContent {
    @JacksonXmlProperty
    private String fromusername;
    @JacksonXmlProperty
    private String encryptusername;
    @JacksonXmlProperty
    private String fromnickname;
    @JacksonXmlProperty
    private String content;
    @JacksonXmlProperty
    private String shortpy;
    @JacksonXmlProperty
    private long imagestatus;
    @JacksonXmlProperty
    private int scene;
    @JacksonXmlProperty
    private String country;
    @JacksonXmlProperty
    private String province;
    @JacksonXmlProperty
    private String city;
    @JacksonXmlProperty
    private int percard;
    @JacksonXmlProperty
    private int sex;
    @JacksonXmlProperty
    private String alias;
    @JacksonXmlProperty
    private long albumflag;
    @JacksonXmlProperty
    private int albumstyle;
    @JacksonXmlProperty
    private String albumbgimgid;
    @JacksonXmlProperty
    private long snsflag;
    @JacksonXmlProperty
    private String snsbgimgid;
    @JacksonXmlProperty
    private String snsbgobjectid;
    @JacksonXmlProperty
    private String mhash;
    @JacksonXmlProperty
    private String mfullhash;
    @JacksonXmlProperty
    private String bigheadimgurl;
    @JacksonXmlProperty
    private String smallheadimgurl;
    @JacksonXmlProperty
    private String ticket;
    @JacksonXmlProperty
    private int opcode;

    public String getFromusername() {
        return fromusername;
    }

    public void setFromusername(String fromusername) {
        this.fromusername = fromusername;
    }

    public String getEncryptusername() {
        return encryptusername;
    }

    public void setEncryptusername(String encryptusername) {
        this.encryptusername = encryptusername;
    }

    public String getFromnickname() {
        return fromnickname;
    }

    public void setFromnickname(String fromnickname) {
        this.fromnickname = fromnickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortpy() {
        return shortpy;
    }

    public void setShortpy(String shortpy) {
        this.shortpy = shortpy;
    }

    public long getImagestatus() {
        return imagestatus;
    }

    public void setImagestatus(long imagestatus) {
        this.imagestatus = imagestatus;
    }

    public int getScene() {
        return scene;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPercard() {
        return percard;
    }

    public void setPercard(int percard) {
        this.percard = percard;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public long getAlbumflag() {
        return albumflag;
    }

    public void setAlbumflag(long albumflag) {
        this.albumflag = albumflag;
    }

    public int getAlbumstyle() {
        return albumstyle;
    }

    public void setAlbumstyle(int albumstyle) {
        this.albumstyle = albumstyle;
    }

    public String getAlbumbgimgid() {
        return albumbgimgid;
    }

    public void setAlbumbgimgid(String albumbgimgid) {
        this.albumbgimgid = albumbgimgid;
    }

    public long getSnsflag() {
        return snsflag;
    }

    public void setSnsflag(long snsflag) {
        this.snsflag = snsflag;
    }

    public String getSnsbgimgid() {
        return snsbgimgid;
    }

    public void setSnsbgimgid(String snsbgimgid) {
        this.snsbgimgid = snsbgimgid;
    }

    public String getSnsbgobjectid() {
        return snsbgobjectid;
    }

    public void setSnsbgobjectid(String snsbgobjectid) {
        this.snsbgobjectid = snsbgobjectid;
    }

    public String getMhash() {
        return mhash;
    }

    public void setMhash(String mhash) {
        this.mhash = mhash;
    }

    public String getMfullhash() {
        return mfullhash;
    }

    public void setMfullhash(String mfullhash) {
        this.mfullhash = mfullhash;
    }

    public String getBigheadimgurl() {
        return bigheadimgurl;
    }

    public void setBigheadimgurl(String bigheadimgurl) {
        this.bigheadimgurl = bigheadimgurl;
    }

    public String getSmallheadimgurl() {
        return smallheadimgurl;
    }

    public void setSmallheadimgurl(String smallheadimgurl) {
        this.smallheadimgurl = smallheadimgurl;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }
}
