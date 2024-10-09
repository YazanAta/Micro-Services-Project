package com.microservices.serviceone.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GovernorateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Governorate getGovernorateSample1() {
        return new Governorate().id(1L).name("name1").population(1);
    }

    public static Governorate getGovernorateSample2() {
        return new Governorate().id(2L).name("name2").population(2);
    }

    public static Governorate getGovernorateRandomSampleGenerator() {
        return new Governorate().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).population(intCount.incrementAndGet());
    }
}
