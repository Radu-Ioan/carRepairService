package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.Car;
import com.carrepairservice.app.service.dto.CarDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {}
