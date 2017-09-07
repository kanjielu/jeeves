package com.cherry.jeeves.utils;

import java.util.Random;

public class DeviceIdGenerator {

    private final static Random random = new Random();

    public static String generate() {
        long rnd = (long) (random.nextDouble() * 1e15);
        return String.format("e%015d", rnd);
    }
}
