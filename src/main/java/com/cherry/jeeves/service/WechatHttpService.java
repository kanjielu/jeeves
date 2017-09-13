package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.request.*;
import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.*;
import com.cherry.jeeves.enums.MessageType;
import com.cherry.jeeves.enums.OpLogCmdId;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.DeviceIdGenerator;
import com.cherry.jeeves.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @Value("${wechat.url.create_chatroom}")
    private String WECHAT_URL_CREATE_CHATROOM;


    private final RestTemplate restTemplate;
    private final RestTemplate redirectableRestTemplate;
    private final HttpHeaders header;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    public WechatHttpService(RestTemplate restTemplate, RestTemplate redirectableRestTemplate, @Value("${wechat.ua}") String USER_AGENT) {
        this.restTemplate = restTemplate;
        this.redirectableRestTemplate = redirectableRestTemplate;
        this.header = new HttpHeaders();
        header.set(HttpHeaders.USER_AGENT, USER_AGENT);
        header.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    }

    public void logout(String hostUrl, String skey) throws IOException, RestClientException {
        final String url = String.format(WECHAT_URL_LOGOUT, hostUrl, escape(skey));
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), Object.class);
    }

    public byte[] getQR(String uuid) throws RestClientException {
        final String url = WECHAT_URL_QRCODE + "/" + uuid;
        ResponseEntity<byte[]> responseEntity
                = redirectableRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), new ParameterizedTypeReference<byte[]>() {
        });
        return responseEntity.getBody();
    }

    public String getUUID() throws RestClientException {
        final String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
        final String url = String.format(WECHAT_URL_UUID, System.currentTimeMillis());
        final String successCode = "200";
        ResponseEntity<String> responseEntity
                = redirectableRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), String.class);
        String body = responseEntity.getBody();

        Matcher matcher = Pattern.compile(regEx).matcher(body);
        if (matcher.find()) {
            if (successCode.equals(matcher.group(1))) {
                return matcher.group(2);
            }
        }
        throw new WechatException("uuid can't be found");
    }

    public LoginResult login(String uuid) throws WechatException, RestClientException {
        final Pattern pattern = Pattern.compile("window.code=(\\d+)");
        Pattern hostUrlPattern = Pattern.compile("window.redirect_uri=\\\"(.*)\\/cgi-bin");
        Pattern redirectUrlPattern = Pattern.compile("window.redirect_uri=\\\"(.*)\\\";");
        long time = System.currentTimeMillis();
        final String url = String.format(WECHAT_URL_LOGIN, uuid, RandomUtils.generateDateWithBitwiseNot(time), time);
        ResponseEntity<String> responseEntity
                = redirectableRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), String.class);
        String body = responseEntity.getBody();
        Matcher matcher = pattern.matcher(body);
        LoginResult response = new LoginResult();
        if (matcher.find()) {
            response.setCode(matcher.group(1));
        } else {
            throw new WechatException("code can't be found");
        }
        Matcher hostUrlMatcher = hostUrlPattern.matcher(body);
        if (hostUrlMatcher.find()) {
            response.setHostUrl(hostUrlMatcher.group(1));
        }
        Matcher redirectUrlMatcher = redirectUrlPattern.matcher(body);
        if (redirectUrlMatcher.find()) {
            response.setRedirectUrl(redirectUrlMatcher.group(1));
        }
        return response;
    }

    public Token redirectLogin(String redirectUrl) throws IOException, RestClientException {
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(redirectUrl, HttpMethod.GET, new HttpEntity<>(header), String.class);
        String xmlString = responseEntity.getBody();
        ObjectMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xmlString, Token.class);
    }

    public SyncCheckResponse syncCheck(String hostUrl, String uin, String sid, String skey, SyncKey syncKey) throws IOException, URISyntaxException, RestClientException {
        final Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}");

        final String path = String.format(WECHAT_URL_SYNC_CHECK, hostUrl);
        URIBuilder builder = new URIBuilder(path);
        builder.addParameter("uin", uin);
        builder.addParameter("sid", sid);
        builder.addParameter("skey", skey);
        builder.addParameter("deviceid", DeviceIdGenerator.generate());
        builder.addParameter("synckey", syncKey.toString());
        builder.addParameter("r", String.valueOf(System.currentTimeMillis()));
        builder.addParameter("_", String.valueOf(System.currentTimeMillis()));
        final URI uri = builder.build().toURL().toURI();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(header), String.class);
        String body = responseEntity.getBody();
        Matcher matcher = pattern.matcher(body);
        if (!matcher.find()) {
            return null;
        } else {
            SyncCheckResponse result = new SyncCheckResponse();
            result.setRetcode(Integer.valueOf(matcher.group(1)));
            result.setSelector(Integer.valueOf(matcher.group(2)));
            return result;
        }
    }

    public GetContactResponse getContact(String hostUrl, String skey, long seq) throws IOException, RestClientException {
        long rnd = System.currentTimeMillis();
        final String url = String.format(WECHAT_URL_GET_CONTACT, hostUrl, rnd, seq, escape(skey));
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), GetContactResponse.class);
    }

    public VerifyUserResponse acceptFriend(String hostUrl, BaseRequest baseRequest, String passTicket, VerifyUser[] verifyUsers) throws IOException, URISyntaxException, RestClientException {
        final int opCode = 3;
        final int[] sceneList = new int[]{33};
        final String path = String.format(WECHAT_URL_VERIFY_USER, hostUrl);
        VerifyUserRequest request = new VerifyUserRequest();
        request.setBaseRequest(baseRequest);
        request.setOpcode(opCode);
        request.setSceneList(sceneList);
        request.setSceneListCount(sceneList.length);
        request.setSkey(baseRequest.getSkey());
        request.setVerifyContent("");
        request.setVerifyUserList(verifyUsers);
        request.setVerifyUserListSize(verifyUsers.length);

        URIBuilder builder = new URIBuilder(path);
        builder.addParameter("r", String.valueOf(System.currentTimeMillis()));
        builder.addParameter("pass_ticket", passTicket);
        final URI uri = builder.build().toURL().toURI();

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), VerifyUserResponse.class);
    }

    public void sendAppMsg() throws RestClientException {
        throw new NotImplementedException();
    }

    public SendMsgResponse sendTextMsg(String hostUrl, BaseRequest baseRequest, String content, String fromUserName, String toUserName) throws IOException, RestClientException {
        final int scene = 0;
        final String rnd = String.valueOf(System.currentTimeMillis() * 10);
        final String url = String.format(WECHAT_URL_SEND_MSG, hostUrl);
        SendMsgRequest request = new SendMsgRequest();
        request.setBaseRequest(baseRequest);
        request.setScene(scene);
        BaseMsg msg = new BaseMsg();
        msg.setType(MessageType.TEXT.getCode());
        msg.setClientMsgId(rnd);
        msg.setContent(content);
        msg.setFromUserName(fromUserName);
        msg.setToUserName(toUserName);
        msg.setLocalID(rnd);
        request.setMsg(msg);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), SendMsgResponse.class);
    }

    public void sendImageMsg() throws RestClientException {
        throw new NotImplementedException();
    }

    public OpLogResponse setAlias(String hostUrl, String passTicket, BaseRequest baseRequest, String newAlias, String userName) throws IOException, RestClientException {
        final int cmdId = OpLogCmdId.MODREMARKNAME.getCode();
        final String url = String.format(WECHAT_URL_OP_LOG, hostUrl, passTicket);
        OpLogRequest request = new OpLogRequest();
        request.setBaseRequest(baseRequest);
        request.setCmdId(cmdId);
        request.setRemarkName(newAlias);
        request.setUserName(userName);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), OpLogResponse.class);
    }

    public BatchGetContactResponse batchGetContact(String hostUrl, BaseRequest baseRequest, String passTicket, ChatRoomDescription[] list) throws IOException, RestClientException {
        long rnd = System.currentTimeMillis();
        String url = String.format(WECHAT_URL_BATCH_GET_CONTACT, hostUrl, rnd, passTicket);
        BatchGetContactRequest request = new BatchGetContactRequest();
        request.setBaseRequest(baseRequest);
        request.setCount(list.length);
        request.setList(list);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), BatchGetContactResponse.class);
    }

    public InitResponse init(String hostUrl, BaseRequest baseRequest, String passTicket) throws IOException, RestClientException {
        String url = String.format(WECHAT_URL_INIT, hostUrl, RandomUtils.generateDateWithBitwiseNot(), passTicket);
        InitRequest request = new InitRequest();
        request.setBaseRequest(baseRequest);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), InitResponse.class);
    }

    //msgtype =37,加我好友
    public SyncResponse sync(String hostUrl, SyncKey syncKey, BaseRequest baseRequest, String passTicket) throws IOException, RestClientException {
        final String url = String.format(WECHAT_URL_SYNC, hostUrl, baseRequest.getSid(), escape(baseRequest.getSkey()), passTicket);
        SyncRequest request = new SyncRequest();
        request.setBaseRequest(baseRequest);
        request.setRr(-System.currentTimeMillis() / 1000);
        request.setSyncKey(syncKey);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), SyncResponse.class);
    }

    public StatusNotifyResponse statusNotify(String hostUrl, String passTicket, BaseRequest baseRequest, String userName, int code) throws IOException, RestClientException {
        String rnd = String.valueOf(System.currentTimeMillis());
        final String url = String.format(WECHAT_URL_STATUS_NOTIFY, hostUrl, passTicket);
        StatusNotifyRequest request = new StatusNotifyRequest();
        request.setBaseRequest(baseRequest);
        request.setFromUserName(userName);
        request.setToUserName(userName);
        request.setCode(code);
        request.setClientMsgId(rnd);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), StatusNotifyResponse.class);
    }

    public createChatRoomResponse createChatRoom(String hostUrl, String passTicket, BaseRequest baseRequest, String[] usernames, String topic) throws IOException {
        String rnd = String.valueOf(System.currentTimeMillis());
        final String url = String.format(hostUrl, rnd, passTicket);
        createChatRoomRequest request = new createChatRoomRequest();
        request.setBaseRequest(baseRequest);
        request.setMemberCount(usernames.length);
        ChatRoomMember[] members = new ChatRoomMember[usernames.length];
        for (int i = 0; i < usernames.length; i++) {
            members[i] = new ChatRoomMember();
            members[i].setUserName(usernames[i]);
        }
        request.setMemberList(members);
        request.setTopic(topic);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.header), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), createChatRoomResponse.class);
    }

    private String escape(String str) throws IOException {
        return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
    }
}