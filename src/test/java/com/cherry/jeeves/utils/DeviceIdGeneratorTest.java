package com.cherry.jeeves.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class DeviceIdGeneratorTest {
    @Test
    public void generate() throws Exception {
        Pattern pattern = Pattern.compile("^e\\d{15}$");
        for (int i = 0; i < 100; i++) {
            Assert.assertTrue(pattern.matcher(DeviceIdGenerator.generate()).find());
        }
    }
}