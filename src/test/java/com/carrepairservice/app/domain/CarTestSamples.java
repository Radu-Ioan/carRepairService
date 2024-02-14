package com.carrepairservice.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Car getCarSample1() {
        return new Car().id(1L).company("company1").manufacturedYear(1).ownerName("ownerName1");
    }

    public static Car getCarSample2() {
        return new Car().id(2L).company("company2").manufacturedYear(2).ownerName("ownerName2");
    }

    public static Car getCarRandomSampleGenerator() {
        return new Car()
            .id(longCount.incrementAndGet())
            .company(UUID.randomUUID().toString())
            .manufacturedYear(intCount.incrementAndGet())
            .ownerName(UUID.randomUUID().toString());
    }
}
