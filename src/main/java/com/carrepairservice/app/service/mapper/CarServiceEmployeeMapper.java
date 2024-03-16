package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarServiceEmployee} and its DTO {@link CarServiceEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarServiceEmployeeMapper extends EntityMapper<CarServiceEmployeeDTO, CarServiceEmployee> {
    @Mapping(target = "carService", source = "carService", qualifiedByName = "carServiceAddress")
    @Mapping(target = "repairAppointments", source = "repairAppointments", qualifiedByName = "carRepairAppointmentIdSet")
    CarServiceEmployeeDTO toDto(CarServiceEmployee s);

    @Mapping(target = "removeRepairAppointments", ignore = true)
    CarServiceEmployee toEntity(CarServiceEmployeeDTO carServiceEmployeeDTO);

    @Named("carServiceAddress")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    CarServiceDTO toDtoCarServiceAddress(CarService carService);

    @Named("carRepairAppointmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CarRepairAppointmentDTO toDtoCarRepairAppointmentId(CarRepairAppointment carRepairAppointment);

    @Named("carRepairAppointmentIdSet")
    default Set<CarRepairAppointmentDTO> toDtoCarRepairAppointmentIdSet(Set<CarRepairAppointment> carRepairAppointment) {
        return carRepairAppointment.stream().map(this::toDtoCarRepairAppointmentId).collect(Collectors.toSet());
    }
}
