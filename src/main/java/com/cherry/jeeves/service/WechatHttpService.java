package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.request.*;
import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.*;
import com.cherry.jeeves.enums.MessageType;
import com.cherry.jeeves.enums.ResultEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
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


    @Value("${wechat.ua}")
    private String USER_AGENT;
    private static final Logger logger = LoggerFactory.getLogger(WechatHttpService.class);
    private final RestTemplate restTemplate;
    private final HttpHeaders header;

    @Autowired
    public WechatHttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.header = new HttpHeaders();
        header.set(HttpHeaders.USER_AGENT, USER_AGENT);
    }

    public void logout(String hostUrl, String skey) throws RestClientException {
        final String url = String.format(WECHAT_URL_LOGOUT, hostUrl, skey);
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), Object.class);
    }

    public byte[] getQR(String uuid) throws RestClientException {
        final String url = WECHAT_URL_QRCODE + uuid;
        ResponseEntity<byte[]> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), new ParameterizedTypeReference<byte[]>() {
        });
        return responseEntity.getBody();
    }

    public String getUUID() throws RestClientException {
        final String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
        final String url = String.format(WECHAT_URL_UUID, System.currentTimeMillis());
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), String.class);
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
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), String.class);
        String body = responseEntity.getBody();
        Matcher matcher = pattern.matcher(body);
        String code = null;
        if (matcher.find()) {
            code = matcher.group(1);
        }
        return code;
    }

    public RedirectLoginResult redirectLogin(String loginContent) throws IOException, RestClientException {
        Pattern hostUrlPattern = Pattern.compile("window.redirect_uri=(.*)\\/cgi-bin");
        Pattern redirectUrlPattern = Pattern.compile("window.redirect_uri=\\\"(.*)\\\";");
        Matcher hostUrlMatcher = hostUrlPattern.matcher(loginContent);
        Matcher redirectUrlMatcher = redirectUrlPattern.matcher(loginContent);
        if (hostUrlMatcher.find() && redirectUrlMatcher.find()) {
            String hostUrl = hostUrlMatcher.group(1);
            String redirectUrl = redirectUrlMatcher.group(1);
            ResponseEntity<String> responseEntity
                    = restTemplate.exchange(redirectUrl, HttpMethod.GET, new HttpEntity<>(header), String.class);
            String xmlString = responseEntity.getBody();
            ObjectMapper xmlMapper = new XmlMapper();
            Token token = xmlMapper.readValue(xmlString, Token.class);
            RedirectLoginResult result = new RedirectLoginResult();
            result.setToken(token);
            result.setHostUrl(hostUrl);
            return result;
        }
        throw new IOException("redirectLogin failed");
    }

    public SyncCheckResponse syncCheck(String hostUrl, BaseRequest baseRequest, SyncKey syncKey) throws RestClientException {
        final Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}");
        long rnd = new Date().getTime();
        final String url = String.format(WECHAT_URL_SYNC_CHECK, hostUrl,
                baseRequest.getUin(), baseRequest.getSid(), baseRequest.getSkey(), baseRequest.getDeviceID(), rnd, syncKey, rnd);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), String.class);
        String body = responseEntity.getBody();
        Matcher matcher = pattern.matcher(body);
        if (!matcher.find()) {
            return null;
        } else {
            SyncCheckResponse result = new SyncCheckResponse();
            result.setRetcode(matcher.group(1));
            result.setSelector(matcher.group(2));
            return result;
        }
    }

    public GetContactResponse getContact(String hostUrl, BaseRequest baseRequest) throws RestClientException {
        long rnd = new Date().getTime();
        final String url = String.format(WECHAT_URL_GET_CONTACT, hostUrl, rnd, baseRequest.getSkey());
        ResponseEntity<GetContactResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), GetContactResponse.class);
        return responseEntity.getBody();
    }

    public VerifyUserResponse acceptFriend(String hostUrl, BaseRequest baseRequest, String passTicket, VerifyUser[] verifyUsers) throws RestClientException {
        long rnd = System.currentTimeMillis() / 3158L;
        final String url = String.format(WECHAT_URL_VERIFY_USER, hostUrl, rnd, passTicket);
        VerifyUserRequest request = new VerifyUserRequest();
        request.setBaseRequest(baseRequest);
        request.setOpcode(3);
        request.setSceneList(new int[]{33});
        request.setSceneListCount(1);
        request.setSkey(baseRequest.getSkey());
        request.setVerifyContent("");
        request.setVerifyUserList(verifyUsers);
        request.setVerifyUserListSize(verifyUsers.length);
        ResponseEntity<VerifyUserResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(request, this.header), VerifyUserResponse.class);
        return responseEntity.getBody();
    }

    public void sendAppMsg() throws RestClientException {
        throw new NotImplementedException();
    }

    public SendMsgResponse sendTextMsg(String hostUrl, BaseRequest baseRequest, String content, String fromUserName, String toUserName) throws RestClientException {
        final String rnd = String.valueOf(new Date().getTime() * 10);
        final String url = String.format(WECHAT_URL_SEND_MSG, hostUrl);
        SendMsgRequest request = new SendMsgRequest();
        request.setBaseRequest(baseRequest);
        request.setScene(0);
        BaseMsg msg = new BaseMsg();
        msg.setType(MessageType.TEXT.getCode());
        msg.setClientMsgId(rnd);
        msg.setContent(content);
        msg.setFromUserName(fromUserName);
        msg.setToUserName(toUserName);
        msg.setLocalID(rnd);
        request.setMsg(msg);
        ResponseEntity<SendMsgResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(request, this.header), SendMsgResponse.class);
        return responseEntity.getBody();
    }

    public void sendImageMsg() throws RestClientException {
        throw new NotImplementedException();
    }

    public OpLogResponse setAlias(String hostUrl, String passTicket, BaseRequest baseRequest, String newAlias, String userName) throws RestClientException {
        final String url = String.format(WECHAT_URL_OP_LOG, hostUrl, passTicket);
        OpLogRequest request = new OpLogRequest();
        request.setBaseRequest(baseRequest);
        request.setCmdId(2);
        request.setRemarkName(newAlias);
        request.setUserName(userName);
        ResponseEntity<OpLogResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(request, this.header), OpLogResponse.class);
        return responseEntity.getBody();
    }

    public BatchGetContactResponse batchGetContact(String hostUrl, String passTicket, BaseRequest baseRequest, ChatRoomDescription[] list) throws RestClientException {
        long rnd = new Date().getTime();
        String url = String.format(WECHAT_URL_BATCH_GET_CONTACT, hostUrl, rnd, passTicket);
        BatchGetContactRequest request = new BatchGetContactRequest();
        request.setBaseRequest(baseRequest);
        request.setCount(list.length);
        request.setList(list);
        ResponseEntity<BatchGetContactResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(request, this.header), BatchGetContactResponse.class);
        return responseEntity.getBody();
    }

    public InitResponse init(String hostUrl, BaseRequest baseRequest, String passTicket) throws RestClientException {
        long rnd = System.currentTimeMillis() / 3158L;
        String url = String.format(WECHAT_URL_INIT, hostUrl, rnd, passTicket);
        ResponseEntity<InitResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(baseRequest), InitResponse.class);
        return responseEntity.getBody();
    }

    //msgtype =37,加我好友
    public SyncResponse sync(String hostUrl, SyncKey syncKey, BaseRequest baseRequest, String passTicket) throws RestClientException {
        final String url = String.format(WECHAT_URL_SYNC, hostUrl, baseRequest.getSid(), baseRequest.getSkey(), passTicket);
        SyncRequest request = new SyncRequest();
        request.setBaseRequest(baseRequest);
        request.setRr(-new Date().getTime() / 1000);
        request.setSyncKey(syncKey);
        ResponseEntity<SyncResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(request, this.header), SyncResponse.class);
        return responseEntity.getBody();
    }

    public StatusNotifyResponse statusNotify(String passTicket, BaseRequest baseRequest, String userName) throws RestClientException {
        String rnd = String.valueOf(System.currentTimeMillis());
        final String url = String.format(WECHAT_URL_STATUS_NOTIFY, passTicket);
        StatusNotifyRequest request = new StatusNotifyRequest();
        request.setBaseRequest(baseRequest);
        request.setFromUserName(userName);
        request.setToUserName(userName);
        request.setCode(3);
        request.setClientMsgId(rnd);
        ResponseEntity<StatusNotifyResponse> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(request, this.header), StatusNotifyResponse.class);
        return responseEntity.getBody();
    }
}