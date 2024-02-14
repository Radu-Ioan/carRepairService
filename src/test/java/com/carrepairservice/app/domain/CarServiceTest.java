package com.carrepairservice.app.domain;

import static com.carrepairservice.app.domain.CarServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
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
}
