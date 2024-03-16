package com.carrepairservice.app.service.impl;

import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.repository.CarServiceEmployeeRepository;
import com.carrepairservice.app.service.CarServiceEmployeeService;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import com.carrepairservice.app.service.mapper.CarServiceEmployeeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.carrepairservice.app.domain.CarServiceEmployee}.
 */
@Service
@Transactional
public class CarServiceEmployeeServiceImpl implements CarServiceEmployeeService {

    private final Logger log = LoggerFactory.getLogger(CarServiceEmployeeServiceImpl.class);

    private final CarServiceEmployeeRepository carServiceEmployeeRepository;

    private final CarServiceEmployeeMapper carServiceEmployeeMapper;

    public CarServiceEmployeeServiceImpl(
        CarServiceEmployeeRepository carServiceEmployeeRepository,
        CarServiceEmployeeMapper carServiceEmployeeMapper
    ) {
        this.carServiceEmployeeRepository = carServiceEmployeeRepository;
        this.carServiceEmployeeMapper = carServiceEmployeeMapper;
    }

    @Override
    public CarServiceEmployeeDTO save(CarServiceEmployeeDTO carServiceEmployeeDTO) {
        log.debug("Request to save CarServiceEmployee : {}", carServiceEmployeeDTO);
        CarServiceEmployee carServiceEmployee = carServiceEmployeeMapper.toEntity(carServiceEmployeeDTO);
        carServiceEmployee = carServiceEmployeeRepository.save(carServiceEmployee);
        return carServiceEmployeeMapper.toDto(carServiceEmployee);
    }

    @Override
    public CarServiceEmployeeDTO update(CarServiceEmployeeDTO carServiceEmployeeDTO) {
        log.debug("Request to update CarServiceEmployee : {}", carServiceEmployeeDTO);
        CarServiceEmployee carServiceEmployee = carServiceEmployeeMapper.toEntity(carServiceEmployeeDTO);
        carServiceEmployee = carServiceEmployeeRepository.save(carServiceEmployee);
        return carServiceEmployeeMapper.toDto(carServiceEmployee);
    }

    @Override
    public Optional<CarServiceEmployeeDTO> partialUpdate(CarServiceEmployeeDTO carServiceEmployeeDTO) {
        log.debug("Request to partially update CarServiceEmployee : {}", carServiceEmployeeDTO);

        return carServiceEmployeeRepository
            .findById(carServiceEmployeeDTO.getId())
            .map(existingCarServiceEmployee -> {
                carServiceEmployeeMapper.partialUpdate(existingCarServiceEmployee, carServiceEmployeeDTO);

                return existingCarServiceEmployee;
            })
            .map(carServiceEmployeeRepository::save)
            .map(carServiceEmployeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarServiceEmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarServiceEmployees");
        return carServiceEmployeeRepository.findAll(pageable).map(carServiceEmployeeMapper::toDto);
    }

    public Page<CarServiceEmployeeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return carServiceEmployeeRepository.findAllWithEagerRelationships(pageable).map(carServiceEmployeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarServiceEmployeeDTO> findOne(Long id) {
        log.debug("Request to get CarServiceEmployee : {}", id);
        return carServiceEmployeeRepository.findOneWithEagerRelationships(id).map(carServiceEmployeeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarServiceEmployee : {}", id);
        carServiceEmployeeRepository.deleteById(id);
    }
}
