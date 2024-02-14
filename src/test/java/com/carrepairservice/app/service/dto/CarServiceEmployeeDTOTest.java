package com.carrepairservice.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarServiceEmployeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarServiceEmployeeDTO.class);
        CarServiceEmployeeDTO carServiceEmployeeDTO1 = new CarServiceEmployeeDTO();
        carServiceEmployeeDTO1.setId(1L);
        CarServiceEmployeeDTO carServiceEmployeeDTO2 = new CarServiceEmployeeDTO();
        assertThat(carServiceEmployeeDTO1).isNotEqualTo(carServiceEmployeeDTO2);
        carServiceEmployeeDTO2.setId(carServiceEmployeeDTO1.getId());
        assertThat(carServiceEmployeeDTO1).isEqualTo(carServiceEmployeeDTO2);
        carServiceEmployeeDTO2.setId(2L);
        assertThat(carServiceEmployeeDTO1).isNotEqualTo(carServiceEmployeeDTO2);
        carServiceEmployeeDTO1.setId(null);
        assertThat(carServiceEmployeeDTO1).isNotEqualTo(carServiceEmployeeDTO2);
    }
}
