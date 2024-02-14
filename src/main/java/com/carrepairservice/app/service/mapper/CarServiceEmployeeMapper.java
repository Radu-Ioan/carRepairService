package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarServiceEmployee} and its DTO {@link CarServiceEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarServiceEmployeeMapper extends EntityMapper<CarServiceEmployeeDTO, CarServiceEmployee> {}
