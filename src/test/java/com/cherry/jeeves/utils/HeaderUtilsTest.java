package com.cherry.jeeves.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HeaderUtilsTest {


    @Test(expected = IllegalArgumentException.class)
    public void mergeWithIllegalArgument() throws Exception {
        HeaderUtils.merge(null);
    }

    @Test
    public void mergeOne() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpHeaders result = HeaderUtils.merge(headers);
        Assert.assertEquals(headers.getContentType(), result.getContentType());
    }

    @Test
    public void mergeTwoWithDifferentNames() throws Exception {
        HttpHeaders header1 = new HttpHeaders();
        header1.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpHeaders header2 = new HttpHeaders();
        header2.setAccessControlMaxAge(100);
        HttpHeaders result = HeaderUtils.merge(header1, header2);
        Assert.assertEquals(header1.getContentType(), result.getContentType());
        Assert.assertEquals(header2.getAccessControlMaxAge(), result.getAccessControlMaxAge());
    }

    @Test
    public void mergeTwoWithSameNames() throws Exception {
        HttpHeaders header1 = new HttpHeaders();
        header1.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpHeaders header2 = new HttpHeaders();
        header2.setContentType(MediaType.APPLICATION_XML);
        HttpHeaders result = HeaderUtils.merge(header1, header2);
        Assert.assertEquals(header2.getContentType(), result.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignWithIllegalArgument1() throws Exception {
        HeaderUtils.assign(new HttpHeaders(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignWithIllegalArgument2() throws Exception {
        HeaderUtils.assign(null, new HttpHeaders());
    }

    @Test
    public void assignWithNone() throws Exception {
        HttpHeaders target = new HttpHeaders();
        target.setContentType(MediaType.TEXT_HTML);
        HeaderUtils.assign(target);
        Assert.assertEquals(MediaType.TEXT_HTML, target.getContentType());
    }

    @Test
    public void assignWithOne() throws Exception {
        HttpHeaders target = new HttpHeaders();
        target.setContentType(MediaType.TEXT_HTML);
        HttpHeaders addon1 = new HttpHeaders();
        addon1.setContentType(MediaType.APPLICATION_XML);
        addon1.setAccessControlMaxAge(100);
        HeaderUtils.assign(target, addon1);
        Assert.assertEquals(target.getContentType(), addon1.getContentType());
        Assert.assertEquals(target.getAccessControlMaxAge(), addon1.getAccessControlMaxAge());
    }

    @Test
    public void assignWithTwo() throws Exception {
        HttpHeaders target = new HttpHeaders();
        target.setContentType(MediaType.TEXT_HTML);
        HttpHeaders addon1 = new HttpHeaders();
        addon1.setContentType(MediaType.APPLICATION_XML);
        HttpHeaders addon2 = new HttpHeaders();
        addon2.setAccessControlMaxAge(100);
        HeaderUtils.assign(target, addon1, addon2);
        Assert.assertEquals(target.getContentType(), addon1.getContentType());
        Assert.assertEquals(target.getAccessControlMaxAge(), addon2.getAccessControlMaxAge());
    }
}