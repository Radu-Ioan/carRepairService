package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarRepairAppointmentTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceEmployeeTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CarServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarService.class);
        CarService carService1 = getCarServiceSample1();
        CarService carService2 = new CarService();
        assertThat(carService1).isNotEqualTo(carService2);

        carService2.setId(carService1.getId());
        assertThat(carService1).isEqualTo(carService2);

        carService2 = getCarServiceSample2();
        assertThat(carService1).isNotEqualTo(carService2);
    }

    @Test
    void repairAppointmentsTest() throws Exception {
        CarService carService = getCarServiceRandomSampleGenerator();
        CarRepairAppointment carRepairAppointmentBack = getCarRepairAppointmentRandomSampleGenerator();

        carService.addRepairAppointments(carRepairAppointmentBack);
        assertThat(carService.getRepairAppointments()).containsOnly(carRepairAppointmentBack);
        assertThat(carRepairAppointmentBack.getCarService()).isEqualTo(carService);

        carService.removeRepairAppointments(carRepairAppointmentBack);
        assertThat(carService.getRepairAppointments()).doesNotContain(carRepairAppointmentBack);
        assertThat(carRepairAppointmentBack.getCarService()).isNull();

        carService.repairAppointments(new HashSet<>(Set.of(carRepairAppointmentBack)));
        assertThat(carService.getRepairAppointments()).containsOnly(carRepairAppointmentBack);
        assertThat(carRepairAppointmentBack.getCarService()).isEqualTo(carService);

        carService.setRepairAppointments(new HashSet<>());
        assertThat(carService.getRepairAppointments()).doesNotContain(carRepairAppointmentBack);
        assertThat(carRepairAppointmentBack.getCarService()).isNull();
    }

    @Test
    void employeesTest() throws Exception {
        CarService carService = getCarServiceRandomSampleGenerator();
        CarServiceEmployee carServiceEmployeeBack = getCarServiceEmployeeRandomSampleGenerator();

        carService.addEmployees(carServiceEmployeeBack);
        assertThat(carService.getEmployees()).containsOnly(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getCarService()).isEqualTo(carService);

        carService.removeEmployees(carServiceEmployeeBack);
        assertThat(carService.getEmployees()).doesNotContain(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getCarService()).isNull();

        carService.employees(new HashSet<>(Set.of(carServiceEmployeeBack)));
        assertThat(carService.getEmployees()).containsOnly(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getCarService()).isEqualTo(carService);

        carService.setEmployees(new HashSet<>());
        assertThat(carService.getEmployees()).doesNotContain(carServiceEmployeeBack);
        assertThat(carServiceEmployeeBack.getCarService()).isNull();
    }
}
