package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarRepairAppointmentTestSamples.*;
import static com.carrepairservice.app.domain.CarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarRepairAppointmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarRepairAppointment.class);
        CarRepairAppointment carRepairAppointment1 = getCarRepairAppointmentSample1();
        CarRepairAppointment carRepairAppointment2 = new CarRepairAppointment();
        assertThat(carRepairAppointment1).isNotEqualTo(carRepairAppointment2);

        carRepairAppointment2.setId(carRepairAppointment1.getId());
        assertThat(carRepairAppointment1).isEqualTo(carRepairAppointment2);

        carRepairAppointment2 = getCarRepairAppointmentSample2();
        assertThat(carRepairAppointment1).isNotEqualTo(carRepairAppointment2);
    }

    @Test
    void carTest() throws Exception {
        CarRepairAppointment carRepairAppointment = getCarRepairAppointmentRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        carRepairAppointment.setCar(carBack);
        assertThat(carRepairAppointment.getCar()).isEqualTo(carBack);

        carRepairAppointment.car(null);
        assertThat(carRepairAppointment.getCar()).isNull();
    }
}
