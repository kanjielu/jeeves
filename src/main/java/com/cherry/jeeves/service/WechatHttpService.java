package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.SyncCheckResult;
import com.cherry.jeeves.enums.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WechatHttpService {

    @Value("${wechat.url.uuid}")
    private String WECHAT_URL_UUID;
    @Value("${wechat.url.qrcode}")
    private String WECHAT_URL_QRCODE;
    @Value("${wechat.url.status_notify}")
    private String WECHAT_URL_STATUS_NOTIFY;
    @Value("${wechat.url.login}")
    private String WECHAT_URL_LOGIN;
    @Value("${wechat.url.init}")
    private String WECHAT_URL_INIT;
    @Value("${wechat.url.sync_check}")
    private String WECHAT_URL_SYNC_CHECK;
    @Value("${wechat.url.sync}")
    private String WECHAT_URL_SYNC;
    @Value("${wechat.url.get_contact}")
    private String WECHAT_URL_GET_CONTACT;
    @Value("${wechat.url.send_msg}")
    private String WECHAT_URL_SEND_MSG;
    @Value("${wechat.url.upload_media}")
    private String WECHAT_URL_UPLOAD_MEDIA;
    @Value("${wechat.url.get_msg_img}")
    private String WECHAT_URL_GET_MSG_IMG;
    @Value("${wechat.url.get_voice}")
    private String WECHAT_URL_GET_VOICE;
    @Value("${wechat.url.get_video}")
    private String WECHAT_URL_GET_VIDEO;
    @Value("${wechat.url.push_login}")
    private String WECHAT_URL_PUSH_LOGIN;
    @Value("${wechat.url.logout}")
    private String WECHAT_URL_LOGOUT;
    @Value("${wechat.url.batch_get_contact}")
    private String WECHAT_URL_BATCH_GET_CONTACT;
    @Value("${wechat.url.op_log}")
    private String WECHAT_URL_OP_LOG;
    @Value("${wechat.url.verify_user}")
    private String WECHAT_URL_VERIFY_USER;
    @Value("${wechat.url.get_media}")
    private String WECHAT_URL_GET_MEDIA;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WechatHttpService.class);

    public void logout(String hostUrl, String skey) throws RestClientException {
        final String url = String.format(WECHAT_URL_LOGOUT, hostUrl, skey);
        restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
    }

    public byte[] getQR(String uuid) throws RestClientException {
        final String url = WECHAT_URL_QRCODE + uuid;
        ResponseEntity<byte[]> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<byte[]>() {
        });
        return responseEntity.getBody();
    }

    public String getUUID() throws RestClientException {
        final String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
        final String url = String.format(WECHAT_URL_UUID, System.currentTimeMillis());
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String body = responseEntity.getBody();

        Matcher matcher = Pattern.compile(regEx).matcher(body);
        if (matcher.find()) {
            if ((ResultEnum.SUCCESS.getCode().equals(matcher.group(1)))) {
                return matcher.group(2);
            }
        }
        return null;
    }

    /**
     * Login Request
     *
     * @param uuid uuid
     * @return code in the response - 200: Success; 201: Await for confirmation on mobile; 408: Await to scan QR
     * @throws RestClientException
     */
    public String login(String uuid) throws RestClientException {
        final Pattern pattern = Pattern.compile("window.code=(\\d+)");
        long time = System.currentTimeMillis();
        final String url = String.format(WECHAT_URL_LOGIN, uuid, time / 1579L, time);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String body = responseEntity.getBody();
        Matcher matcher = pattern.matcher(body);
        String code = null;
        if (matcher.find()) {
            code = matcher.group(1);
        }
        return code;
    }

    public void processLoginInfo() {
        //TODO
    }

    public SyncCheckResult syncCheck(String hostUrl, String uin, String sid, String skey, String deviceId, String syncKey) throws RestClientException {
        final Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}");
        long rnd = new Date().getTime();
        final String url = String.format(WECHAT_URL_SYNC_CHECK, hostUrl, uin, sid, skey, deviceId, rnd, syncKey, rnd);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String body = responseEntity.getBody();
        Matcher matcher = pattern.matcher(body);
        if (!matcher.find()) {
            return null;
        } else {
            SyncCheckResult result = new SyncCheckResult();
            result.setRetcode(matcher.group(1));
            result.setSelector(matcher.group(2));
            return result;
        }
    }

    public void getContact(String hostUrl) throws RestClientException {
        //TODO
        final String url = String.format(WECHAT_URL_GET_CONTACT, hostUrl);
    }

    public void acceptFriend() throws RestClientException {
        //TODO
    }
}