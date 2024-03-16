package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarRepairAppointmentTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceEmployeeTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CarServiceEmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarServiceEmployee.class);
        CarServiceEmployee carServiceEmployee1 = getCarServiceEmployeeSample1();
        CarServiceEmployee carServiceEmployee2 = new CarServiceEmployee();
        assertThat(carServiceEmployee1).isNotEqualTo(carServiceEmployee2);

        carServiceEmployee2.setId(carServiceEmployee1.getId());
        assertThat(carServiceEmployee1).isEqualTo(carServiceEmployee2);

        carServiceEmployee2 = getCarServiceEmployeeSample2();
        assertThat(carServiceEmployee1).isNotEqualTo(carServiceEmployee2);
    }

    @Test
    void carServiceTest() throws Exception {
        CarServiceEmployee carServiceEmployee = getCarServiceEmployeeRandomSampleGenerator();
        CarService carServiceBack = getCarServiceRandomSampleGenerator();

        carServiceEmployee.setCarService(carServiceBack);
        assertThat(carServiceEmployee.getCarService()).isEqualTo(carServiceBack);

        carServiceEmployee.carService(null);
        assertThat(carServiceEmployee.getCarService()).isNull();
    }

    @Test
    void repairAppointmentsTest() throws Exception {
        CarServiceEmployee carServiceEmployee = getCarServiceEmployeeRandomSampleGenerator();
        CarRepairAppointment carRepairAppointmentBack = getCarRepairAppointmentRandomSampleGenerator();

        carServiceEmployee.addRepairAppointments(carRepairAppointmentBack);
        assertThat(carServiceEmployee.getRepairAppointments()).containsOnly(carRepairAppointmentBack);

        carServiceEmployee.removeRepairAppointments(carRepairAppointmentBack);
        assertThat(carServiceEmployee.getRepairAppointments()).doesNotContain(carRepairAppointmentBack);

        carServiceEmployee.repairAppointments(new HashSet<>(Set.of(carRepairAppointmentBack)));
        assertThat(carServiceEmployee.getRepairAppointments()).containsOnly(carRepairAppointmentBack);

        carServiceEmployee.setRepairAppointments(new HashSet<>());
        assertThat(carServiceEmployee.getRepairAppointments()).doesNotContain(carRepairAppointmentBack);
    }
}
