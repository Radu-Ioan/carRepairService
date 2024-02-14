package com.carrepairservice.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CarService getCarServiceSample1() {
        return new CarService().id(1L).name("name1").address("address1");
    }

    public static CarService getCarServiceSample2() {
        return new CarService().id(2L).name("name2").address("address2");
    }

    public static CarService getCarServiceRandomSampleGenerator() {
        return new CarService().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).address(UUID.randomUUID().toString());
    }
}
