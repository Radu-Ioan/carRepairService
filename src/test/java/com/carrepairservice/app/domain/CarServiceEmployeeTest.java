package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarServiceEmployeeTestSamples.*;
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
}
