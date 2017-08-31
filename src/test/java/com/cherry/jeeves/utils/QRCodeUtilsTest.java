package com.cherry.jeeves.utils;

import org.junit.Test;

public class QRCodeUtilsTest {
    @Test
    public void decode() throws Exception {
//        String filePath = "E://qr//qr.jpg";
//        InputStream stream = new FileInputStream(filePath);
//        Assert.assertEquals(QRCodeUtils.decode(stream), "https://login.weixin.qq.com/l/Qag24GEVRw==");
//        stream.close();
    }

    @Test
    public void generateQR() throws Exception {
        final String url = "https://github.com/";
        String qrcode = QRCodeUtils.generateQR(url, 5, 5);
        System.out.print(qrcode);
    }
}