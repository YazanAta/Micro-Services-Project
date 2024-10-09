package com.microservices.serviceone.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BrigadeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Brigade getBrigadeSample1() {
        return new Brigade().id(1L).name("name1").type("type1").establishedYear(1);
    }

    public static Brigade getBrigadeSample2() {
        return new Brigade().id(2L).name("name2").type("type2").establishedYear(2);
    }

    public static Brigade getBrigadeRandomSampleGenerator() {
        return new Brigade()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .establishedYear(intCount.incrementAndGet());
    }
}
