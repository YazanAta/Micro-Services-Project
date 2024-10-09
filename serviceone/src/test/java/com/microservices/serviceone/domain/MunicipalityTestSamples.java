package com.microservices.serviceone.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MunicipalityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Municipality getMunicipalitySample1() {
        return new Municipality().id(1L).name("name1");
    }

    public static Municipality getMunicipalitySample2() {
        return new Municipality().id(2L).name("name2");
    }

    public static Municipality getMunicipalityRandomSampleGenerator() {
        return new Municipality().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
