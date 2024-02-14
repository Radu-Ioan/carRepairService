package com.carrepairservice.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarServiceDTO.class);
        CarServiceDTO carServiceDTO1 = new CarServiceDTO();
        carServiceDTO1.setId(1L);
        CarServiceDTO carServiceDTO2 = new CarServiceDTO();
        assertThat(carServiceDTO1).isNotEqualTo(carServiceDTO2);
        carServiceDTO2.setId(carServiceDTO1.getId());
        assertThat(carServiceDTO1).isEqualTo(carServiceDTO2);
        carServiceDTO2.setId(2L);
        assertThat(carServiceDTO1).isNotEqualTo(carServiceDTO2);
        carServiceDTO1.setId(null);
        assertThat(carServiceDTO1).isNotEqualTo(carServiceDTO2);
    }
}
