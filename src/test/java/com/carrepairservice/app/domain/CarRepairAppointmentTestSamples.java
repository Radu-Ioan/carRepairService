package com.carrepairservice.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CarRepairAppointmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CarRepairAppointment getCarRepairAppointmentSample1() {
        return new CarRepairAppointment().id(1L);
    }

    public static CarRepairAppointment getCarRepairAppointmentSample2() {
        return new CarRepairAppointment().id(2L);
    }

    public static CarRepairAppointment getCarRepairAppointmentRandomSampleGenerator() {
        return new CarRepairAppointment().id(longCount.incrementAndGet());
    }
}
