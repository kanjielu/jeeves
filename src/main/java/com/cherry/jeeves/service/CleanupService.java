package com.cherry.jeeves.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanupService implements DisposableBean {

    @Autowired
    private WechatHttpServiceInternal wechatHttpService;
    @Autowired
    private CacheService cacheService;

    private static final Logger logger = LoggerFactory.getLogger(CleanupService.class);

    @Override
    public void destroy() throws Exception {
        logger.warn("[*] system is being destroyed");
        if (cacheService.isAlive()) {
            try {
                logger.warn("[*] logging out");
                wechatHttpService.logout(cacheService.getHostUrl(), cacheService.getsKey());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
