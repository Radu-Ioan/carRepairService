package com.carrepairservice.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarServiceEmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CarServiceEmployee getCarServiceEmployeeSample1() {
        return new CarServiceEmployee().id(1L).name("name1").age(1).yearsOfExperience(1);
    }

    public static CarServiceEmployee getCarServiceEmployeeSample2() {
        return new CarServiceEmployee().id(2L).name("name2").age(2).yearsOfExperience(2);
    }

    public static CarServiceEmployee getCarServiceEmployeeRandomSampleGenerator() {
        return new CarServiceEmployee()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .age(intCount.incrementAndGet())
            .yearsOfExperience(intCount.incrementAndGet());
    }
}
