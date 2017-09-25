package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.request.*;
import com.cherry.jeeves.domain.request.component.BaseRequest;
import com.cherry.jeeves.domain.response.*;
import com.cherry.jeeves.domain.shared.*;
import com.cherry.jeeves.enums.*;
import com.cherry.jeeves.exception.WechatException;
import com.cherry.jeeves.utils.DeviceIdGenerator;
import com.cherry.jeeves.utils.HeaderUtils;
import com.cherry.jeeves.utils.RandomUtils;
import com.cherry.jeeves.utils.WechatUtils;
import com.cherry.jeeves.utils.rest.StatefullRestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    @Value("${wechat.url.statreport}")
    private String WECHAT_URL_STATREPORT;
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
    private final HttpHeaders postHeader;
    private final HttpHeaders getHeader;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private String originValue = null;
    private String refererValue = null;
    private String BROWSER_DEFAULT_ACCEPT_LANGUAGE = "en,zh-CN;q=0.8,zh;q=0.6,ja;q=0.4,zh-TW;q=0.2";
    private String BROWSER_DEFAULT_ACCEPT_ENCODING = "gzip, deflate, br";

    @Autowired
    WechatHttpServiceInternal(RestTemplate restTemplate, @Value("${wechat.ua}") String BROWSER_DEFAULT_USER_AGENT) {
        this.restTemplate = restTemplate;
        this.postHeader = new HttpHeaders();
        postHeader.set(HttpHeaders.USER_AGENT, BROWSER_DEFAULT_USER_AGENT);
        postHeader.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        postHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL));
        postHeader.set(HttpHeaders.ACCEPT_LANGUAGE, BROWSER_DEFAULT_ACCEPT_LANGUAGE);
        postHeader.set(HttpHeaders.ACCEPT_ENCODING, BROWSER_DEFAULT_ACCEPT_ENCODING);
        this.getHeader = new HttpHeaders();
        getHeader.set(HttpHeaders.USER_AGENT, BROWSER_DEFAULT_USER_AGENT);
        getHeader.set(HttpHeaders.ACCEPT_LANGUAGE, BROWSER_DEFAULT_ACCEPT_LANGUAGE);
        getHeader.set(HttpHeaders.ACCEPT_ENCODING, BROWSER_DEFAULT_ACCEPT_ENCODING);
    }

    void logout(String hostUrl, String skey) throws IOException {
        final String url = String.format(WECHAT_URL_LOGOUT, hostUrl, escape(skey));
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(postHeader), Object.class);
    }

    /**
     * Open the entry page.
     *
     * @param retryTimes retry times of qr scan
     */
    void open(int retryTimes) {
        final String url = WECHAT_URL_ENTRY;
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setPragma("no-cache");
        customHeader.setCacheControl("no-cache");
        customHeader.set("Upgrade-Insecure-Requests", "1");
        customHeader.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        HeaderUtils.assign(customHeader, getHeader);
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
        //manually insert two cookies into cookiestore, as they're supposed to be stored in browsers by javascript.
        CookieStore store = (CookieStore) ((StatefullRestTemplate) restTemplate).getHttpContext().getAttribute(HttpClientContext.COOKIE_STORE);
        Date maxDate = new Date(Long.MAX_VALUE);
        String domain = WECHAT_URL_ENTRY.replaceAll("https://", "").replaceAll("/", "");
        Map<String, String> cookies = new HashMap<>(3);
        cookies.put("MM_WX_NOTIFY_STATE", "1");
        cookies.put("MM_WX_SOUND_STATE", "1");
        if (retryTimes > 0) {
            cookies.put("refreshTimes", String.valueOf(retryTimes));
        }
        appendAdditionalCookies(store, cookies, domain, "/", maxDate);
        //It's now at entry page.
        this.originValue = WECHAT_URL_ENTRY;
        this.refererValue = WECHAT_URL_ENTRY.replaceAll("/$", "");
    }

    /**
     * Get UUID for this session
     *
     * @return UUID
     */
    String getUUID() {
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

    /**
     * Get QR code for scanning
     *
     * @param uuid UUID
     * @return QR code in binary
     */
    byte[] getQR(String uuid) {
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

    /**
     * Get hostUrl and redirectUrl
     *
     * @param uuid
     * @return hostUrl and redirectUrl
     * @throws WechatException if the response doesn't contain code
     */
    LoginResult login(String uuid) throws WechatException {
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

    /**
     * Get basic parameters for this session
     *
     * @param redirectUrl
     * @return session token
     * @throws IOException if the http response body can't be convert to {@link Token}
     */
    Token openNewloginpage(String redirectUrl) throws IOException {
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

    /**
     * Redirect to main page of wechat
     *
     * @param hostUrl hostUrl
     */
    void redirect(String hostUrl) {
        final String url = hostUrl + "/";
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        customHeader.set("Upgrade-Insecure-Requests", "1");
        HeaderUtils.assign(customHeader, getHeader);
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
        //It's now at main page.
        this.originValue = hostUrl;
        this.refererValue = hostUrl + "/";
    }

    /**
     * Initialization
     *
     * @param hostUrl     hostUrl
     * @param baseRequest baseRequest
     * @return current user's information and contact information
     * @throws IOException if the http response body can't be convert to {@link InitResponse}
     */
    InitResponse init(String hostUrl, BaseRequest baseRequest) throws IOException {
        String url = String.format(WECHAT_URL_INIT, hostUrl, RandomUtils.generateDateWithBitwiseNot());

        CookieStore store = (CookieStore) ((StatefullRestTemplate) restTemplate).getHttpContext().getAttribute(HttpClientContext.COOKIE_STORE);
        Date maxDate = new Date(Long.MAX_VALUE);
        String domain = hostUrl.replaceAll("https://", "").replaceAll("/", "");
        Map<String, String> cookies = new HashMap<>(3);
        cookies.put("MM_WX_NOTIFY_STATE", "1");
        cookies.put("MM_WX_SOUND_STATE", "1");
        appendAdditionalCookies(store, cookies, domain, "/", maxDate);
        InitRequest request = new InitRequest();
        request.setBaseRequest(baseRequest);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        customHeader.setOrigin(hostUrl);
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), InitResponse.class);
    }

    /**
     * Notify mobile side once certain actions have been taken on web side.
     *
     * @param hostUrl     hostUrl
     * @param baseRequest baseRequest
     * @param userName    the userName of the user
     * @param code        {@link StatusNotifyCode}
     * @return the http response body
     * @throws IOException if the http response body can't be convert to {@link StatusNotifyResponse}
     */
    StatusNotifyResponse statusNotify(String hostUrl, BaseRequest baseRequest, String userName, int code) throws IOException {
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
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), StatusNotifyResponse.class);
    }

    /**
     * report stats to server
     */
    void statReport() {
        final String url = WECHAT_URL_STATREPORT;
        StatReportRequest request = new StatReportRequest();
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setUin("");
        baseRequest.setSid("");
        request.setBaseRequest(baseRequest);
        request.setCount(0);
        request.setList(new StatReport[0]);
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set(HttpHeaders.REFERER, WECHAT_URL_ENTRY);
        customHeader.setOrigin(WECHAT_URL_ENTRY.replaceAll("/$", ""));
        HeaderUtils.assign(customHeader, postHeader);
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
    }

    /**
     * Get all the contacts. If the Seq it returns is greater than zero, it means at least one more request is required to fetch all contacts.
     *
     * @param hostUrl hostUrl
     * @param skey    skey
     * @param seq     seq
     * @return contact information
     * @throws IOException if the http response body can't be convert to {@link GetContactResponse}
     */
    GetContactResponse getContact(String hostUrl, String skey, long seq) throws IOException {
        long rnd = System.currentTimeMillis();
        final String url = String.format(WECHAT_URL_GET_CONTACT, hostUrl, rnd, seq, escape(skey));
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL));
        customHeader.set(HttpHeaders.REFERER, hostUrl + "/");
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), GetContactResponse.class);
    }

    /**
     * Get all the members in the given chatrooms
     *
     * @param hostUrl     hostUrl
     * @param baseRequest baseRequest
     * @param list        chatroom information
     * @return chatroom members information
     * @throws IOException if the http response body can't be convert to {@link BatchGetContactResponse}
     */
    BatchGetContactResponse batchGetContact(String hostUrl, BaseRequest baseRequest, ChatRoomDescription[] list) throws IOException {
        long rnd = System.currentTimeMillis();
        String url = String.format(WECHAT_URL_BATCH_GET_CONTACT, hostUrl, rnd);
        BatchGetContactRequest request = new BatchGetContactRequest();
        request.setBaseRequest(baseRequest);
        request.setCount(list.length);
        request.setList(list);
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), BatchGetContactResponse.class);
    }

    /**
     * Periodically request to server
     *
     * @param hostUrl hostUrl
     * @param uin     uin
     * @param sid     sid
     * @param skey    skey
     * @param syncKey syncKey
     * @return synccheck response
     * @throws IOException        if the http response body can't be convert to {@link SyncCheckResponse}
     * @throws URISyntaxException if url is invalid
     */
    SyncCheckResponse syncCheck(String hostUrl, String uin, String sid, String skey, SyncKey syncKey) throws IOException, URISyntaxException {
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

    /**
     * Sync with server to get new messages and contacts
     *
     * @param hostUrl     hostUrl
     * @param syncKey     syncKey
     * @param baseRequest baseRequest
     * @return new messages and contacts
     * @throws IOException if the http response body can't be convert to {@link SyncResponse}
     */
    SyncResponse sync(String hostUrl, SyncKey syncKey, BaseRequest baseRequest) throws IOException {
        final String url = String.format(WECHAT_URL_SYNC, hostUrl, baseRequest.getSid(), escape(baseRequest.getSkey()));
        SyncRequest request = new SyncRequest();
        request.setBaseRequest(baseRequest);
        request.setRr(-System.currentTimeMillis() / 1000);
        request.setSyncKey(syncKey);
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), SyncResponse.class);
    }

    VerifyUserResponse acceptFriend(String hostUrl, BaseRequest baseRequest, String passTicket, VerifyUser[] verifyUsers) throws IOException, URISyntaxException {
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
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), VerifyUserResponse.class);
    }

    SendMsgResponse sendText(String hostUrl, BaseRequest baseRequest, String content, String fromUserName, String toUserName) throws IOException {
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
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), SendMsgResponse.class);
    }

    OpLogResponse setAlias(String hostUrl, BaseRequest baseRequest, String newAlias, String userName) throws IOException {
        final int cmdId = OpLogCmdId.MODREMARKNAME.getCode();
        final String url = String.format(WECHAT_URL_OP_LOG, hostUrl);
        OpLogRequest request = new OpLogRequest();
        request.setBaseRequest(baseRequest);
        request.setCmdId(cmdId);
        request.setRemarkName(newAlias);
        request.setUserName(userName);
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), OpLogResponse.class);
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
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), CreateChatRoomResponse.class);
    }

    DeleteChatRoomMemberResponse deleteChatRoomMember(String hostUrl, BaseRequest baseRequest, String chatRoomUserName, String userName) throws IOException {
        final String url = String.format(WECHAT_URL_DELETE_CHATROOM_MEMBER, hostUrl);
        DeleteChatRoomMemberRequest request = new DeleteChatRoomMemberRequest();
        request.setBaseRequest(baseRequest);
        request.setChatRoomName(chatRoomUserName);
        request.setDelMemberList(userName);
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), DeleteChatRoomMemberResponse.class);
    }

    AddChatRoomMemberResponse addChatRoomMember(String hostUrl, BaseRequest baseRequest, String chatRoomUserName, String userName) throws IOException {
        final String url = String.format(WECHAT_URL_ADD_CHATROOM_MEMBER, hostUrl);
        AddChatRoomMemberRequest request = new AddChatRoomMemberRequest();
        request.setBaseRequest(baseRequest);
        request.setChatRoomName(chatRoomUserName);
        request.setAddMemberList(userName);
        HttpHeaders customHeader = createPostCustomHeader();
        HeaderUtils.assign(customHeader, postHeader);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, customHeader), String.class);
        return jsonMapper.readValue(WechatUtils.textDecode(responseEntity.getBody()), AddChatRoomMemberResponse.class);
    }

    byte[] downloadImage(String url) {
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        customHeader.set("Referer", this.refererValue);
        HeaderUtils.assign(customHeader, getHeader);
        ResponseEntity<byte[]> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(customHeader), new ParameterizedTypeReference<byte[]>() {
        });
        return responseEntity.getBody();
    }

    private String escape(String str) throws IOException {
        return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
    }

    private void appendAdditionalCookies(CookieStore store, Map<String, String> cookies, String domain, String path, Date expiryDate) {
        cookies.forEach((key, value) -> {
            BasicClientCookie cookie = new BasicClientCookie(key, value);
            cookie.setDomain(domain);
            cookie.setPath(path);
            cookie.setExpiryDate(expiryDate);
            store.addCookie(cookie);
        });
    }

    private HttpHeaders createPostCustomHeader() {
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setOrigin(this.originValue);
        customHeader.set(HttpHeaders.REFERER, this.refererValue);
        return customHeader;
    }
}