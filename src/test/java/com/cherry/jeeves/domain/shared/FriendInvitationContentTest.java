package com.cherry.jeeves.domain.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class FriendInvitationContentTest {

    @Test
    public void TestDeserializing() throws IOException {
        String xmlString = "&lt;msg fromusername=\"wxid_emf5glqo1tlf22\" encryptusername=\"v1_632d08f0036f3610315c0aeb1db8825f6392c4b5755ad2c99214ad2c447fa1ea7f7dff79999c6e64bfe2d69578c8eeb0@stranger\" fromnickname=\"鹧鸪仔\" content=\"我是鹧鸪仔\"  shortpy=\"ZGZ\" imagestatus=\"3\" scene=\"30\" country=\"\" province=\"\" city=\"\" sign=\"\" percard=\"1\" sex=\"0\" alias=\"daishuxiaogege\" weibo=\"\" weibonickname=\"\" albumflag=\"0\" albumstyle=\"0\" albumbgimgid=\"\" snsflag=\"1\" snsbgimgid=\"\" snsbgobjectid=\"0\" mhash=\"86ed45196ad6ee51f69661a68ab4e689\" mfullhash=\"86ed45196ad6ee51f69661a68ab4e689\" bigheadimgurl=\"http://wx.qlogo.cn/mmhead/ver_1/5TrzicKwZW9a9Rc7YhJrRhnw5ZO46eEvvNhE8xicqwmubcTrtg9Dowp6b8BQKiaEO3JLWViarLPib2Zfic9VA2ETUZokucgFvhy6aaQNcVjAk3mBM/0\" smallheadimgurl=\"http://wx.qlogo.cn/mmhead/ver_1/5TrzicKwZW9a9Rc7YhJrRhnw5ZO46eEvvNhE8xicqwmubcTrtg9Dowp6b8BQKiaEO3JLWViarLPib2Zfic9VA2ETUZokucgFvhy6aaQNcVjAk3mBM/132\" ticket=\"v2_52ca9d554ec220ad0882373288b6e14cc717dd91ad8db6bb38c64df1774e933c6d026f4168cadf877a38f6c4ec1cf3e79088eef110f53ae8d57474d6665c1497@stranger\" opcode=\"2\" googlecontact=\"\" qrticket=\"\" chatroomusername=\"\" sourceusername=\"\" sourcenickname=\"\"&gt;&lt;brandlist count=\"0\" ver=\"693400187\"&gt;&lt;/brandlist&gt;&lt;/msg&gt;";
        ObjectMapper xmlMapper = new XmlMapper();
        FriendInvitationContent content = xmlMapper.readValue(StringEscapeUtils.unescapeXml(xmlString), FriendInvitationContent.class);
        Assert.assertTrue(content.getFromusername().equals("wxid_emf5glqo1tlf22"));
    }
}