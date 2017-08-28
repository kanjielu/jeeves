package com.cherry.jeeves.service;

import com.cherry.jeeves.domain.response.SyncCheckResponse;
import com.cherry.jeeves.domain.response.SyncResponse;
import com.cherry.jeeves.enums.RetCode;
import com.cherry.jeeves.enums.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SyncServie {
    private static final Logger logger = LoggerFactory.getLogger(SyncServie.class);

    @Autowired
    private CacheService cacheService;
    @Autowired
    private WechatHttpService wechatHttpService;

    public void sync() throws IOException {
        SyncCheckResponse syncCheckResponse = wechatHttpService.syncCheck(cacheService.getHostUrl(), cacheService.getBaseRequest(), cacheService.getSyncKey());
        String retCode = syncCheckResponse.getRetcode();
        int selector = syncCheckResponse.getSelector();
        logger.info("[*] synccheck retcode = " + retCode);
        logger.info("[*] synccheck selector = " + selector);
        if (retCode.equals(RetCode.NORMAL.getCode())) {
            if (selector == Selector.NEW_MESSAGE.getCode()) {
                SyncResponse syncResponse = syncInternal();
            }
        } else {
            syncInternal();
        }
    }

    private SyncResponse syncInternal() throws IOException {
        SyncResponse syncResponse = wechatHttpService.sync(cacheService.getHostUrl(), cacheService.getSyncKey(), cacheService.getBaseRequest(), cacheService.getPassTicket());
        cacheService.setSyncKey(syncResponse.getSyncKey());
        cacheService.setSyncCheckKey(syncResponse.getSyncCheckKey());
        return syncResponse;
    }

    private void onChatRoomMessageReceived() {

    }
}
