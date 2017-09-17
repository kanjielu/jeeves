package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.request.*;
import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.*;
import com.cherry.jeeves.enums.AddScene;
import com.cherry.jeeves.enums.MessageType;
import com.cherry.jeeves.enums.OpLogCmdId;
import com.cherry.jeeves.enums.VerifyUserOPCode;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.DeviceIdGenerator;
import com.cherry.jeeves.utils.HeaderUtils;
import com.cherry.jeeves.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class WechatHttpServiceInternal {

    @Value("${wechat.url.entry}")
    private String WECHAT_URL_ENTRY;
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
    @Value("${wechat.url.delete_chatroom_member}")
    private String WECHAT_URL_DELETE_CHATROOM_MEMBER;
    @Value("${wechat.url.add_chatroom_member}")
    private String WECHAT_URL_ADD_CHATROOM_MEMBER;

    private final RestTemplate restTemplate;
    private final RestTemplate redirectableRestTemplate;
    private final HttpHeaders postHeader;
    private final HttpHeaders getHeader;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private String BROWSER_DEFAULT_ACCEPT_LANGUAGE = "en,zh-CN;q=0.8,zh;q=0.6,ja;q=0.4,zh-TW;q=0.2";
    private String BROWSER_DEFAULT_ACCEPT_ENCODING = "gzip, deflate, br";
    private String BROWSER_DEFAULT_CONNECTION = "keep-alive";

    @Autowired
    WechatHttpServiceInternal(RestTemplate restTemplate, RestTemplate redirectableRestTemplate, @Value("${wechat.ua}") String BROWSER_DEFAULT_USER_AGENT) {
        this.restTemplate = restTemplate;
        this.redirectableRestTemplate = redirectableRestTemplate;
        this.postHeader = new HttpHeaders();
        postHeader.set(HttpHeaders.USER_AGENT, BROWSER_DEFAULT_USER_AGENT);
        postHeader.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        postHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL));
        postHeader.set(HttpHeaders.ACCEPT_LANGUAGE, BROWSER_DEFAULT_ACCEPT_LANGUAGE);
        postHeader.set(HttpHeaders.ACCEPT_ENCODING, BROWSER_DEFAULT_ACCEPT_ENCODING);
        postHeader.setConnection(BROWSER_DEFAULT_CONNECTION);
        this.getHeader = new HttpHeaders();
        getHeader.set(HttpHeaders.USER_AGENT, BROWSER_DEFAULT_USER_AGENT);
        getHeader.set(HttpHeaders.ACCEPT_LANGUAGE, BROWSER_DEFAULT_ACCEPT_LANGUAGE);
        getHeader.set(HttpHeaders.ACCEPT_ENCODING, BROWSER_DEFAULT_ACCEPT_ENCODING);
        getHeader.setConnection(BROWSER_DEFAULT_CONNECTION);
    }

    void logout(String hostUrl, String skey) throws IOException, RestClientException {
        final String url = String.format(WECHAT_URL_LOGOUT, hostUrl, escape(skey));
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(postHeader), Object.class);
    }

    void open() throws IOException, RestClientException {
        final String url = WECHAT_URL_ENTRY;
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setPragma("no-cache");
        customHeader.setCacheControl("no-cache");
        customHeader.set("Upgrade-Insecure-Requests", "1");
        customHeader.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        HeaderUtils.assign(customHeader, getHeader);
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), Object.class);
    }

    String getUUID() throws RestClientException {
        final String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
        final String url = String.format(WECHAT_URL_UUID, System.currentTimeMillis());
        final String successCode = "200";
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setPragma("no-cache");
        customHeader.setCacheControl("no-cache");
        customHeader.setAccept(Arrays.asList(MediaType.ALL));
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
        String body = responseEntity.getBody();
        Matcher matcher = Pattern.compile(regEx).matcher(body);
        if (matcher.find()) {
            if (successCode.equals(matcher.group(1))) {
                return matcher.group(2);
            }
        }
        throw new WechatException("uuid can't be found");
    }

    byte[] getQR(String uuid) throws RestClientException {
        final String url = WECHAT_URL_QRCODE + "/" + uuid;
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.ACCEPT, "image/webp,image/apng,image/*,*/*;q=0.8");
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<byte[]> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), new ParameterizedTypeReference<byte[]>() {
        });
        return responseEntity.getBody();
    }

    LoginResult login(String uuid) throws WechatException, RestClientException {
        final Pattern pattern = Pattern.compile("window.code=(\\d+)");
        Pattern hostUrlPattern = Pattern.compile("window.redirect_uri=\\\"(.*)\\/cgi-bin");
        Pattern redirectUrlPattern = Pattern.compile("window.redirect_uri=\\\"(.*)\\\";");
        long time = System.currentTimeMillis();
        final String url = String.format(WECHAT_URL_LOGIN, uuid, RandomUtils.generateDateWithBitwiseNot(time), time);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setAccept(Arrays.asList(MediaType.ALL));
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
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

    Token openNewloginpage(String redirectUrl) throws IOException, RestClientException {
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        customHeader.set("Upgrade-Insecure-Requests", "1");
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(redirectUrl, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
        String xmlString = responseEntity.getBody();
        ObjectMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xmlString, Token.class);
    }

    void redirect(String hostUrl) throws IOException, RestClientException {
        final String url = hostUrl + "/";
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        customHeader.set("Upgrade-Insecure-Requests", "1");
        HeaderUtils.assign(customHeader, getHeader);
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), Object.class);
    }

    InitResponse init(String hostUrl, BaseRequest baseRequest) throws IOException, RestClientException {
        String url = String.format(WECHAT_URL_INIT, hostUrl, RandomUtils.generateDateWithBitwiseNot());
        InitRequest request = new InitRequest();
        request.setBaseRequest(baseRequest);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        customHeader.setOrigin(hostUrl);
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), InitResponse.class);
    }

    //将网页端的状态通知手机端,例如消息已被读等
    StatusNotifyResponse statusNotify(String hostUrl, BaseRequest baseRequest, String userName, int code) throws IOException, RestClientException {
        String rnd = String.valueOf(System.currentTimeMillis());
        final String url = String.format(WECHAT_URL_STATUS_NOTIFY, hostUrl);
        StatusNotifyRequest request = new StatusNotifyRequest();
        request.setBaseRequest(baseRequest);
        request.setFromUserName(userName);
        request.setToUserName(userName);
        request.setCode(code);
        request.setClientMsgId(rnd);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        customHeader.setOrigin(hostUrl);
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), StatusNotifyResponse.class);
    }

    GetContactResponse getContact(String hostUrl, String skey, long seq) throws IOException, RestClientException {
        long rnd = System.currentTimeMillis();
        final String url = String.format(WECHAT_URL_GET_CONTACT, hostUrl, rnd, seq, escape(skey));
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL));
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), GetContactResponse.class);
    }

    BatchGetContactResponse batchGetContact(String hostUrl, BaseRequest baseRequest, ChatRoomDescription[] list) throws IOException, RestClientException {
        long rnd = System.currentTimeMillis();
        String url = String.format(WECHAT_URL_BATCH_GET_CONTACT, hostUrl, rnd);
        BatchGetContactRequest request = new BatchGetContactRequest();
        request.setBaseRequest(baseRequest);
        request.setCount(list.length);
        request.setList(list);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setOrigin(hostUrl);
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), BatchGetContactResponse.class);
    }

    SyncCheckResponse syncCheck(String hostUrl, String uin, String sid, String skey, SyncKey syncKey) throws IOException, URISyntaxException, RestClientException {
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
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setAccept(Arrays.asList(MediaType.ALL));
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
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

    //msgtype =37,加我好友
    SyncResponse sync(String hostUrl, SyncKey syncKey, BaseRequest baseRequest) throws IOException, RestClientException {
        final String url = String.format(WECHAT_URL_SYNC, hostUrl, baseRequest.getSid(), escape(baseRequest.getSkey()));
        SyncRequest request = new SyncRequest();
        request.setBaseRequest(baseRequest);
        request.setRr(-System.currentTimeMillis() / 1000);
        request.setSyncKey(syncKey);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setOrigin(hostUrl);
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), SyncResponse.class);
    }

    VerifyUserResponse acceptFriend(String hostUrl, BaseRequest baseRequest, String passTicket, VerifyUser[] verifyUsers) throws IOException, URISyntaxException, RestClientException {
        final int opCode = VerifyUserOPCode.VERIFYOK.getCode();
        final int[] sceneList = new int[]{AddScene.WEB.getCode()};
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
                = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(request, this.postHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), VerifyUserResponse.class);
    }

    void sendAppMsg() throws RestClientException {
        throw new NotImplementedException();
    }

    SendMsgResponse sendTextMsg(String hostUrl, BaseRequest baseRequest, String content, String fromUserName, String toUserName) throws IOException, RestClientException {
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
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.postHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), SendMsgResponse.class);
    }

    void sendImageMsg() throws RestClientException {
        throw new NotImplementedException();
    }

    OpLogResponse setAlias(String hostUrl, BaseRequest baseRequest, String newAlias, String userName) throws IOException, RestClientException {
        final int cmdId = OpLogCmdId.MODREMARKNAME.getCode();
        final String url = String.format(WECHAT_URL_OP_LOG, hostUrl);
        OpLogRequest request = new OpLogRequest();
        request.setBaseRequest(baseRequest);
        request.setCmdId(cmdId);
        request.setRemarkName(newAlias);
        request.setUserName(userName);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.postHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), OpLogResponse.class);
    }

    CreateChatRoomResponse createChatRoom(String hostUrl, BaseRequest baseRequest, String[] userNames, String topic) throws IOException {
        String rnd = String.valueOf(System.currentTimeMillis());
        final String url = String.format(WECHAT_URL_CREATE_CHATROOM, hostUrl, rnd);
        CreateChatRoomRequest request = new CreateChatRoomRequest();
        request.setBaseRequest(baseRequest);
        request.setMemberCount(userNames.length);
        ChatRoomMember[] members = new ChatRoomMember[userNames.length];
        for (int i = 0; i < userNames.length; i++) {
            members[i] = new ChatRoomMember();
            members[i].setUserName(userNames[i]);
        }
        request.setMemberList(members);
        request.setTopic(topic);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.postHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), CreateChatRoomResponse.class);
    }

    DeleteChatRoomMemberResponse deleteChatRoomMember(String hostUrl, BaseRequest baseRequest, String chatRoomUserName, String userName) throws IOException {
        final String url = String.format(WECHAT_URL_DELETE_CHATROOM_MEMBER, hostUrl);
        DeleteChatRoomMemberRequest request = new DeleteChatRoomMemberRequest();
        request.setBaseRequest(baseRequest);
        request.setChatRoomName(chatRoomUserName);
        request.setDelMemberList(userName);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.postHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), DeleteChatRoomMemberResponse.class);
    }

    AddChatRoomMemberResponse addChatRoomMember(String hostUrl, BaseRequest baseRequest, String chatRoomUserName, String userName) throws IOException {
        final String url = String.format(WECHAT_URL_ADD_CHATROOM_MEMBER, hostUrl);
        AddChatRoomMemberRequest request = new AddChatRoomMemberRequest();
        request.setBaseRequest(baseRequest);
        request.setChatRoomName(chatRoomUserName);
        request.setAddMemberList(userName);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, this.postHeader), String.class);
        return jsonMapper.readValue(responseEntity.getBody(), AddChatRoomMemberResponse.class);
    }

    private String escape(String str) throws IOException {
        return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
    }
}