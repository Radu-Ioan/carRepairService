package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarRepairAppointmentTestSamples.*;
import static com.carrepairservice.app.domain.CarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = getCarSample1();
        Car car2 = new Car();
        assertThat(car1).isNotEqualTo(car2);

        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);

        car2 = getCarSample2();
        assertThat(car1).isNotEqualTo(car2);
    }

    @Test
    void carRepairAppointmentTest() throws Exception {
        Car car = getCarRandomSampleGenerator();
        CarRepairAppointment carRepairAppointmentBack = getCarRepairAppointmentRandomSampleGenerator();

        car.setCarRepairAppointment(carRepairAppointmentBack);
        assertThat(car.getCarRepairAppointment()).isEqualTo(carRepairAppointmentBack);

        car.carRepairAppointment(null);
        assertThat(car.getCarRepairAppointment()).isNull();
    }
}
