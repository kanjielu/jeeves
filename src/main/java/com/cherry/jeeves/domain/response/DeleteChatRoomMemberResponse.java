package com.cherry.jeeves.domain.response;

import com.cherry.jeeves.domain.response.component.WechatHttpResponseBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteChatRoomMemberResponse extends WechatHttpResponseBase {
}
