package com.cherry.jeeves.utils;

import org.junit.Assert;
import org.junit.Test;

public class RandomUtilsTest {
    @Test
    public void generateDateWithBitwiseNot1() throws Exception {
        Assert.assertEquals(-2050223504, RandomUtils.generateDateWithBitwiseNot(1505288777103L));
    }

    @Test
    public void generateDateWithBitwiseNot2() throws Exception {
        Assert.assertEquals(-2049618525, RandomUtils.generateDateWithBitwiseNot(1505288172124L));
    }

    @Test
    public void generateDateWithBitwiseNot3() throws Exception {
        Assert.assertEquals(-2050747166, RandomUtils.generateDateWithBitwiseNot(1505289300765L));
    }

    @Test
    public void generateDateWithBitwiseNot4() throws Exception {
        Assert.assertEquals(-2052031548, RandomUtils.generateDateWithBitwiseNot(1505290585147L));
    }
}