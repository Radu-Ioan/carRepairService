package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarServiceEmployeeTestSamples.*;
import static com.carrepairservice.app.domain.CarServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
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
}
