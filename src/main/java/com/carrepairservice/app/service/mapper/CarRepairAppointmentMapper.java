package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.Car;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.service.dto.CarDTO;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarRepairAppointment} and its DTO {@link CarRepairAppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarRepairAppointmentMapper extends EntityMapper<CarRepairAppointmentDTO, CarRepairAppointment> {
    @Mapping(target = "car", source = "car", qualifiedByName = "carOwnerName")
    CarRepairAppointmentDTO toDto(CarRepairAppointment s);

    @Named("carOwnerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ownerName", source = "ownerName")
    CarDTO toDtoCarOwnerName(Car car);
}
