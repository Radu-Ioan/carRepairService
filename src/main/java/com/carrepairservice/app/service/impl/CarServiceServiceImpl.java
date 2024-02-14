package com.carrepairservice.app.service.impl;

import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.repository.CarServiceRepository;
import com.carrepairservice.app.service.CarServiceService;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import com.carrepairservice.app.service.mapper.CarServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.carrepairservice.app.domain.CarService}.
 */
@Service
@Transactional
public class CarServiceServiceImpl implements CarServiceService {

    private final Logger log = LoggerFactory.getLogger(CarServiceServiceImpl.class);

    private final CarServiceRepository carServiceRepository;

    private final CarServiceMapper carServiceMapper;

    public CarServiceServiceImpl(CarServiceRepository carServiceRepository, CarServiceMapper carServiceMapper) {
        this.carServiceRepository = carServiceRepository;
        this.carServiceMapper = carServiceMapper;
    }

    @Override
    public CarServiceDTO save(CarServiceDTO carServiceDTO) {
        log.debug("Request to save CarService : {}", carServiceDTO);
        CarService carService = carServiceMapper.toEntity(carServiceDTO);
        carService = carServiceRepository.save(carService);
        return carServiceMapper.toDto(carService);
    }

    @Override
    public CarServiceDTO update(CarServiceDTO carServiceDTO) {
        log.debug("Request to update CarService : {}", carServiceDTO);
        CarService carService = carServiceMapper.toEntity(carServiceDTO);
        carService = carServiceRepository.save(carService);
        return carServiceMapper.toDto(carService);
    }

    @Override
    public Optional<CarServiceDTO> partialUpdate(CarServiceDTO carServiceDTO) {
        log.debug("Request to partially update CarService : {}", carServiceDTO);

        return carServiceRepository
            .findById(carServiceDTO.getId())
            .map(existingCarService -> {
                carServiceMapper.partialUpdate(existingCarService, carServiceDTO);

                return existingCarService;
            })
            .map(carServiceRepository::save)
            .map(carServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarServices");
        return carServiceRepository.findAll(pageable).map(carServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarServiceDTO> findOne(Long id) {
        log.debug("Request to get CarService : {}", id);
        return carServiceRepository.findById(id).map(carServiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarService : {}", id);
        carServiceRepository.deleteById(id);
    }
}
