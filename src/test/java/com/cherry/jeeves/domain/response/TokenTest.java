package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.shared.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TokenTest {

    @Test
    public void TestDemoralizing() throws IOException {
        String xmlString = "<error><ret>0</ret><message></message><skey>@crypt_c51c94ad_06b82d2842bdefdae788fbb762d9b631</skey><wxsid>QT6wafhptuVx0gKa</wxsid><wxuin>1983611971</wxuin><pass_ticket>i8PQXWfnMmVX%2Frx0ThkPwapCC0jBOMxV7cjYdgFrW1leTJ28vqDXBzyqkt3RLjbM</pass_ticket><isgrayscale>1</isgrayscale></error>";
        ObjectMapper xmlMapper = new XmlMapper();
        Token token = xmlMapper.readValue(xmlString, Token.class);
        Assert.assertTrue(token.getRet() == 0);
        Assert.assertTrue(token.getSkey().equals("@crypt_c51c94ad_06b82d2842bdefdae788fbb762d9b631"));
    }
}