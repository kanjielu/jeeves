package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.response.InitResponse;
import com.cherry.jeeves.domain.response.LoginResult;
import com.cherry.jeeves.domain.response.StatusNotifyResponse;
import com.cherry.jeeves.domain.response.component.BaseResponse;
import com.cherry.jeeves.domain.shared.Token;
import com.cherry.jeeves.enums.LoginCode;
import com.cherry.jeeves.exception.WechatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;

    public void login() {
        try {
            //1 uuid
            String uuid = wechatHttpService.getUUID();
            cacheService.setUuid(uuid);
            logger.info("[1] uuid completed");
            //2 qr
            byte[] qrData = wechatHttpService.getQR(uuid);
            String path = "E://qr//qr.jpg";
            OutputStream out = new FileOutputStream(path);
            out.write(qrData);
            out.flush();
            out.close();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("cmd /c start " + path);
            logger.info("[2] qrcode completed");
            //3 login
            LoginResult loginResponse = null;
            while (true) {
                loginResponse = wechatHttpService.login(uuid);
                if (LoginCode.SUCCESS.getCode().equals(loginResponse.getCode())) {
                    if (loginResponse.getHostUrl() == null) {
                        throw new WechatException("hostUrl can't be found");
                    }
                    if (loginResponse.getRedirectUrl() == null) {
                        throw new WechatException("redirectUrl can't be found");
                    }
                    cacheService.setHostUrl(loginResponse.getHostUrl());
                    break;
                } else {
                    logger.info("login status = " + loginResponse.getCode());
                }
            }
            logger.info("[3] login completed");
            //4 redirect login
            Token token = wechatHttpService.redirectLogin(loginResponse.getRedirectUrl());
            if (token.getRet() == 0) {
                cacheService.setPassTicket(token.getPass_ticket());
                cacheService.setsKey(token.getSkey());
                cacheService.setSid(token.getWxsid());
                cacheService.setUin(token.getWxuin());
            } else {
                throw new WechatException("token ret = " + token.getRet());
            }
            logger.info("[4] redirect completed");
            //5 init
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setUin(cacheService.getUin());
            baseRequest.setSid(cacheService.getSid());
            baseRequest.setSkey(cacheService.getsKey());
            String rndDeviceId = "e" + String.valueOf(new Random().nextLong()).substring(1, 16);
            baseRequest.setDeviceID(rndDeviceId);
            InitResponse initResponse = wechatHttpService.init(cacheService.getHostUrl(), baseRequest);
            if (!checkBaseResponse(initResponse.getBaseResponse())) {
                throw new WechatException("initResponse ret = " + initResponse.getBaseResponse().getRet());
            }
            //TODO add contacts to cache
            cacheService.setOwner(initResponse.getUser());
            //6 status notify
            StatusNotifyResponse statusNotifyResponse = wechatHttpService.statusNotify(cacheService.getPassTicket(), baseRequest, cacheService.getOwner().getUserName());
            if (!checkBaseResponse(statusNotifyResponse.getBaseResponse())) {
                throw new WechatException("statusNotifyResponse ret = " + statusNotifyResponse.getBaseResponse().getRet());
            }
            logger.info("[5] init completed");
            //scucess

        } catch (IOException ex) {
            throw new WechatException(ex);
        }
    }

    private boolean checkBaseResponse(BaseResponse baseResponse) {
        return baseResponse.getRet() == 0;
    }
}
