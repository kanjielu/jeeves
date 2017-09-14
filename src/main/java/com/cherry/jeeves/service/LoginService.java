package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.ChatRoomDescription;
import com.cherry.jeeves.domain.shared.Token;
import com.cherry.jeeves.enums.LoginCode;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.exception.WechatQRExpiredException;
import com.cherry.jeeves.utils.QRCodeUtils;
import com.cherry.jeeves.utils.WechatUtils;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private CacheService cacheService;
    @Autowired
    private SyncServie syncServie;
    @Autowired
    private WechatHttpServiceInternal wechatHttpService;

    @Value("${jeeves.auto-relogin-when-qrcode-expired}")
    private boolean AUTO_RELOGIN_WHEN_QRCODE_EXPIRED;

    public void login() {
        try {
            //1 uuid
            String uuid = wechatHttpService.getUUID();
            cacheService.setUuid(uuid);
            logger.info("[1] uuid completed");
            //2 qr
            byte[] qrData = wechatHttpService.getQR(uuid);
            ByteArrayInputStream stream = new ByteArrayInputStream(qrData);
            String qrUrl = QRCodeUtils.decode(stream);
            stream.close();
            String qr = QRCodeUtils.generateQR(qrUrl, 40, 40);
            logger.info(qr);
            logger.info("[2] qrcode completed");
            //3 login
            LoginResult loginResponse;
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
                    if (loginResponse.getHostUrl().equals("https://wechat.com")) {
                        cacheService.setSyncUrl("https://webpush.web.wechat.com");
                        cacheService.setFileUrl("https://file.web.wechat.com");
                    } else {
                        cacheService.setSyncUrl(loginResponse.getHostUrl().replaceFirst("^https://", "https://webpush."));
                        cacheService.setFileUrl(loginResponse.getHostUrl().replaceFirst("^https://", "https://file."));
                    }
                    break;
                } else if (LoginCode.AWAIT_CONFIRMATION.getCode().equals(loginResponse.getCode())) {
                    logger.info("[*] login status = AWAIT_CONFIRMATION");
                } else if (LoginCode.AWAIT_SCANNING.getCode().equals(loginResponse.getCode())) {
                    logger.info("[*] login status = AWAIT_SCANNING");
                } else if (LoginCode.EXPIRED.getCode().equals(loginResponse.getCode())) {
                    logger.info("[*] login status = EXPIRED");
                    throw new WechatQRExpiredException();
                } else {
                    logger.info("[*] login status = " + loginResponse.getCode());
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
                BaseRequest baseRequest = new BaseRequest();
                baseRequest.setUin(cacheService.getUin());
                baseRequest.setSid(cacheService.getSid());
                baseRequest.setSkey(cacheService.getsKey());
                cacheService.setBaseRequest(baseRequest);
            } else {
                throw new WechatException("token ret = " + token.getRet());
            }
            logger.info("[4] redirect completed");
            //5 init
            InitResponse initResponse = wechatHttpService.init(cacheService.getHostUrl(), cacheService.getBaseRequest());
            if (!WechatUtils.checkBaseResponse(initResponse.getBaseResponse())) {
                throw new WechatException("initResponse ret = " + initResponse.getBaseResponse().getRet());
            }
            cacheService.setSyncKey(initResponse.getSyncKey());
            cacheService.setOwner(initResponse.getUser());
            logger.info("[5] init completed");
            //6 status notify
            StatusNotifyResponse statusNotifyResponse =
                    wechatHttpService.statusNotify(cacheService.getHostUrl(),
                            cacheService.getBaseRequest(),
                            cacheService.getOwner().getUserName(), 3);
            if (!WechatUtils.checkBaseResponse(statusNotifyResponse.getBaseResponse())) {
                throw new WechatException("statusNotifyResponse ret = " + statusNotifyResponse.getBaseResponse().getRet());
            }
            logger.info("[6] status notify completed");
            //7 get contact
            long seq = 0;
            do {
                GetContactResponse getContactResponse = wechatHttpService.getContact(cacheService.getHostUrl(), cacheService.getBaseRequest().getSkey(), seq);
                if (!WechatUtils.checkBaseResponse(getContactResponse.getBaseResponse())) {
                    throw new WechatException("getContactResponse ret = " + getContactResponse.getBaseResponse().getRet());
                } else {
                    logger.info("[*] getContactResponse seq = " + getContactResponse.getSeq());
                    logger.info("[*] getContactResponse memberCount = " + getContactResponse.getMemberCount());
                    seq = getContactResponse.getSeq();
                    cacheService.getIndividuals().addAll(Arrays.stream(getContactResponse.getMemberList()).filter(WechatUtils::isIndividual).collect(Collectors.toSet()));
                    cacheService.getMediaPlatforms().addAll(Arrays.stream(getContactResponse.getMemberList()).filter(WechatUtils::isMediaPlatform).collect(Collectors.toSet()));
                }
            } while (seq > 0);
            logger.info("[7] get contact completed");
            //8 batch get contact
            ChatRoomDescription[] chatRoomDescriptions = Arrays.stream(initResponse.getContactList())
                    .filter(x -> x != null && WechatUtils.isChatRoom(x))
                    .map(x -> {
                        ChatRoomDescription description = new ChatRoomDescription();
                        description.setUserName(x.getUserName());
                        return description;
                    })
                    .toArray(ChatRoomDescription[]::new);
            if (chatRoomDescriptions.length > 0) {
                BatchGetContactResponse batchGetContactResponse = wechatHttpService.batchGetContact(
                        cacheService.getHostUrl(),
                        cacheService.getBaseRequest(),
                        chatRoomDescriptions);
                if (!WechatUtils.checkBaseResponse(batchGetContactResponse.getBaseResponse())) {
                    throw new WechatException("batchGetContactResponse ret = " + batchGetContactResponse.getBaseResponse().getRet());
                } else {
                    logger.info("[*] batchGetContactResponse count = " + batchGetContactResponse.getCount());
                    cacheService.getChatRooms().addAll(Arrays.asList(batchGetContactResponse.getContactList()));
                }
            }
            logger.info("[8] batch get contact completed");
            cacheService.setAlive(true);
            logger.info("[*] login process completed");
            logger.info("[*] start listening");
            while (true) {
                syncServie.listen();
            }
        } catch (IOException | NotFoundException | WriterException | URISyntaxException ex) {
            throw new WechatException(ex);
        } catch (WechatQRExpiredException ex) {
            if (AUTO_RELOGIN_WHEN_QRCODE_EXPIRED) {
                login();
            } else {
                throw new WechatException(ex);
            }
        }
    }
}
