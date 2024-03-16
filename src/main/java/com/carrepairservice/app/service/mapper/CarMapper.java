package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.Car;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.service.dto.CarDTO;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {
    @Mapping(target = "carRepairAppointment", source = "carRepairAppointment", qualifiedByName = "carRepairAppointmentDate")
    CarDTO toDto(Car s);

    @Named("carRepairAppointmentDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    CarRepairAppointmentDTO toDtoCarRepairAppointmentDate(CarRepairAppointment carRepairAppointment);
}
