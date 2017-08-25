package com.cherry.jeeves;

import com.cherry.jeeves.service.LoginService;
import com.cherry.jeeves.service.WechatHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Jeeves {
    @Autowired
    private LoginService loginService;
    @Value("${wechat.url.login}")
    private String url;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public void start() {
        logger.info("Jeeves starts");
        System.setProperty("jsse.enableSNIExtension", "false");
        loginService.login();
    }
}