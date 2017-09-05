package com.cherry.jeeves.task;

import com.cherry.jeeves.service.CacheService;
import com.cherry.jeeves.service.SyncServie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class SyncTask {

    @Autowired
    private SyncServie syncServie;
    @Autowired
    private CacheService cacheService;

    private static final Logger logger = LoggerFactory.getLogger(SyncTask.class);

    @Scheduled(fixedDelay = 3000)
    public void sync() throws IOException, URISyntaxException {
        if (cacheService.isAlive()) {
            syncServie.listen();
        }
    }
}
