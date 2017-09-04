package com.cherry.jeeves.utils;

import org.junit.Test;

public class QRCodeUtilsTest {

    @Test
    public void generateQR() throws Exception {
        final String url = "https://github.com/";
        String qrcode = QRCodeUtils.generateQR(url, 5, 5);
        System.out.print(qrcode);
    }
}