package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarRepairAppointmentTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceEmployeeTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceTestSamples.*;
import static com.carrepairservice.app.domain.CarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void carServiceTest() throws Exception {
        CarRepairAppointment carRepairAppointment = getCarRepairAppointmentRandomSampleGenerator();
        CarService carServiceBack = getCarServiceRandomSampleGenerator();

        carRepairAppointment.setCarService(carServiceBack);
        assertThat(carRepairAppointment.getCarService()).isEqualTo(carServiceBack);

        carRepairAppointment.carService(null);
        assertThat(carRepairAppointment.getCarService()).isNull();
    }

    @Test
    void responsibleEmployeesTest() throws Exception {
        CarRepairAppointment carRepairAppointment = getCarRepairAppointmentRandomSampleGenerator();
        CarServiceEmployee carServiceEmployeeBack = getCarServiceEmployeeRandomSampleGenerator();

        carRepairAppointment.addResponsibleEmployees(carServiceEmployeeBack);
        assertThat(carRepairAppointment.getResponsibleEmployees()).containsOnly(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getRepairAppointments()).containsOnly(carRepairAppointment);

        carRepairAppointment.removeResponsibleEmployees(carServiceEmployeeBack);
        assertThat(carRepairAppointment.getResponsibleEmployees()).doesNotContain(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getRepairAppointments()).doesNotContain(carRepairAppointment);

        carRepairAppointment.responsibleEmployees(new HashSet<>(Set.of(carServiceEmployeeBack)));
        assertThat(carRepairAppointment.getResponsibleEmployees()).containsOnly(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getRepairAppointments()).containsOnly(carRepairAppointment);

        carRepairAppointment.setResponsibleEmployees(new HashSet<>());
        assertThat(carRepairAppointment.getResponsibleEmployees()).doesNotContain(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getRepairAppointments()).doesNotContain(carRepairAppointment);
    }
}
