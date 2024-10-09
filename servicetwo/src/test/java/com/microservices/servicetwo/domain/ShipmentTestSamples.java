package com.microservices.servicetwo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Shipment getShipmentSample1() {
        return new Shipment().id(1L).trackingNumber("trackingNumber1");
    }

    public static Shipment getShipmentSample2() {
        return new Shipment().id(2L).trackingNumber("trackingNumber2");
    }

    public static Shipment getShipmentRandomSampleGenerator() {
        return new Shipment().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
