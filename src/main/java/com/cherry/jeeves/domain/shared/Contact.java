package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact extends Member {
    @JsonProperty
    private int MemberCount;
    @JsonProperty
    private Set<ChatRoomMember> MemberList;
    @JsonProperty
    private int OwnerUin;
    @JsonProperty
    private long Statues;
    @JsonProperty
    private long AttrStatus;
    @JsonProperty
    private String Province;
    @JsonProperty
    private String City;
    @JsonProperty
    private String Alias;
    @JsonProperty
    private int UniFriend;
    @JsonProperty
    private String DisplayName;
    @JsonProperty
    private long ChatRoomId;
    @JsonProperty
    private String KeyWord;
    @JsonProperty
    private String EncryChatRoomId;
    @JsonProperty
    private int IsOwner;

    public long getStatues() {
        return Statues;
    }

    public void setStatues(long statues) {
        Statues = statues;
    }

    public long getAttrStatus() {
        return AttrStatus;
    }

    public void setAttrStatus(long attrStatus) {
        AttrStatus = attrStatus;
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

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public int getUniFriend() {
        return UniFriend;
    }

    public void setUniFriend(int uniFriend) {
        UniFriend = uniFriend;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public long getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(long chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String keyWord) {
        KeyWord = keyWord;
    }

    public String getEncryChatRoomId() {
        return EncryChatRoomId;
    }

    public void setEncryChatRoomId(String encryChatRoomId) {
        EncryChatRoomId = encryChatRoomId;
    }

    public int getIsOwner() {
        return IsOwner;
    }

    public void setIsOwner(int isOwner) {
        IsOwner = isOwner;
    }

    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public Set<ChatRoomMember> getMemberList() {
        return MemberList;
    }

    public void setMemberList(Set<ChatRoomMember> memberList) {
        MemberList = memberList;
    }

    public int getOwnerUin() {
        return OwnerUin;
    }

    public void setOwnerUin(int ownerUin) {
        OwnerUin = ownerUin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return this.getUserName().equals(contact.getUserName());
    }

    @Override
    public int hashCode() {
        return this.getUserName().hashCode();
    }
}
