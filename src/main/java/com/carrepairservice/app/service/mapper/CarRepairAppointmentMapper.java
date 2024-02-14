package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarRepairAppointment} and its DTO {@link CarRepairAppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarRepairAppointmentMapper extends EntityMapper<CarRepairAppointmentDTO, CarRepairAppointment> {}
