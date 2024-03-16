package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarServiceEmployee} and its DTO {@link CarServiceEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarServiceEmployeeMapper extends EntityMapper<CarServiceEmployeeDTO, CarServiceEmployee> {
    @Mapping(target = "carService", source = "carService", qualifiedByName = "carServiceAddress")
    CarServiceEmployeeDTO toDto(CarServiceEmployee s);

    @Named("carServiceAddress")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    CarServiceDTO toDtoCarServiceAddress(CarService carService);
}
