package com.carrepairservice.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.carrepairservice.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarRepairAppointmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarRepairAppointmentDTO.class);
        CarRepairAppointmentDTO carRepairAppointmentDTO1 = new CarRepairAppointmentDTO();
        carRepairAppointmentDTO1.setId(1L);
        CarRepairAppointmentDTO carRepairAppointmentDTO2 = new CarRepairAppointmentDTO();
        assertThat(carRepairAppointmentDTO1).isNotEqualTo(carRepairAppointmentDTO2);
        carRepairAppointmentDTO2.setId(carRepairAppointmentDTO1.getId());
        assertThat(carRepairAppointmentDTO1).isEqualTo(carRepairAppointmentDTO2);
        carRepairAppointmentDTO2.setId(2L);
        assertThat(carRepairAppointmentDTO1).isNotEqualTo(carRepairAppointmentDTO2);
        carRepairAppointmentDTO1.setId(null);
        assertThat(carRepairAppointmentDTO1).isNotEqualTo(carRepairAppointmentDTO2);
    }
}
