package com.carrepairservice.app.service.mapper;

import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarService} and its DTO {@link CarServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarServiceMapper extends EntityMapper<CarServiceDTO, CarService> {}
