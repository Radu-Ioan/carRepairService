package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.Car;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.service.dto.CarDTO;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarRepairAppointment} and its DTO {@link CarRepairAppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarRepairAppointmentMapper extends EntityMapper<CarRepairAppointmentDTO, CarRepairAppointment> {
    @Mapping(target = "car", source = "car", qualifiedByName = "carOwnerName")
    @Mapping(target = "carService", source = "carService", qualifiedByName = "carServiceAddress")
    CarRepairAppointmentDTO toDto(CarRepairAppointment s);

    @Named("carOwnerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ownerName", source = "ownerName")
    CarDTO toDtoCarOwnerName(Car car);

    @Named("carServiceAddress")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    CarServiceDTO toDtoCarServiceAddress(CarService carService);
}
