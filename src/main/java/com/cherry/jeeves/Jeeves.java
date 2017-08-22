package com.cherry.jeeves;

import com.cherry.jeeves.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Jeeves {
    @Autowired
    private LoginService loginService;
    @Value("${wechat.url.login}")
    private String url;

    public void test(){
        System.out.println(url);
    }
}
