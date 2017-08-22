package com.cherry.jeeves.service;

import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private boolean alive = false;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
