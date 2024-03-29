package com.carrepairservice.app.service.impl;

import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.repository.CarRepairAppointmentRepository;
import com.carrepairservice.app.repository.CarServiceEmployeeRepository;
import com.carrepairservice.app.repository.CarServiceRepository;
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

    private final CarRepairAppointmentRepository carRepairAppointmentRepository;

    private final CarServiceRepository carServiceRepository;

    public CarServiceEmployeeServiceImpl(
        CarServiceEmployeeRepository carServiceEmployeeRepository,
        CarServiceEmployeeMapper carServiceEmployeeMapper,
        CarRepairAppointmentRepository carRepairAppointmentRepository,
        CarServiceRepository carServiceRepository
    ) {
        this.carServiceEmployeeRepository = carServiceEmployeeRepository;
        this.carServiceEmployeeMapper = carServiceEmployeeMapper;
        this.carRepairAppointmentRepository = carRepairAppointmentRepository;
        this.carServiceRepository = carServiceRepository;
    }

    @Override
    public CarServiceEmployeeDTO save(CarServiceEmployeeDTO carServiceEmployeeDTO) {
        log.debug("Request to save CarServiceEmployee : {}", carServiceEmployeeDTO);
        CarServiceEmployee carServiceEmployee = carServiceEmployeeMapper.toEntity(carServiceEmployeeDTO);
        carServiceEmployee = carServiceEmployeeRepository.save(carServiceEmployee);

        var appointments = carServiceEmployee.getRepairAppointments();

        // update Appointments table
        for (var a : appointments) {
            a.getResponsibleEmployees().add(carServiceEmployee);
            // add the new ones
            carRepairAppointmentRepository.save(a);
        }
        for (var a : carRepairAppointmentRepository.findAll()) {
            // delete old
            if (!appointments.contains(a)) {
                a.getResponsibleEmployees().remove(carServiceEmployee);
                carRepairAppointmentRepository.save(a);
            }
        }

        var service = carServiceEmployee.getCarService();
        // update the new one
        service.getEmployees().add(carServiceEmployee);
        carServiceRepository.save(service);

        CarServiceEmployee finalCarServiceEmployee = carServiceEmployee;
        // update the old
        carServiceRepository
            .findAll()
            .forEach(s -> {
                if (!s.getAddress().equals(service.getName())) {
                    s.getEmployees().remove(finalCarServiceEmployee);
                    carServiceRepository.save(s);
                }
            });

        return carServiceEmployeeMapper.toDto(carServiceEmployee);
    }

    @Override
    public CarServiceEmployeeDTO update(CarServiceEmployeeDTO carServiceEmployeeDTO) {
        log.debug("Request to update CarServiceEmployee : {}", carServiceEmployeeDTO);
        CarServiceEmployee carServiceEmployee = carServiceEmployeeMapper.toEntity(carServiceEmployeeDTO);
        carServiceEmployee = carServiceEmployeeRepository.save(carServiceEmployee);

        var appointments = carServiceEmployee.getRepairAppointments();

        for (var a : appointments) {
            a.getResponsibleEmployees().add(carServiceEmployee);
            carRepairAppointmentRepository.save(a);
        }
        for (var a : carRepairAppointmentRepository.findAll()) {
            // delete old
            if (!appointments.contains(a)) {
                a.getResponsibleEmployees().remove(carServiceEmployee);
                carRepairAppointmentRepository.save(a);
            }
        }

        var service = carServiceEmployee.getCarService();
        // update the new one
        service.getEmployees().add(carServiceEmployee);
        carServiceRepository.save(service);

        CarServiceEmployee finalCarServiceEmployee = carServiceEmployee;
        // update the old
        carServiceRepository
            .findAll()
            .forEach(s -> {
                if (!s.getAddress().equals(service.getName())) {
                    s.getEmployees().remove(finalCarServiceEmployee);
                    carServiceRepository.save(s);
                }
            });

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

        var employeeOptional = carServiceEmployeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            var employee = employeeOptional.get();

            var carService = employee.getCarService();
            carService.removeEmployees(employee);
            carServiceRepository.save(carService);

            for (var a : employee.getRepairAppointments()) {
                a.removeResponsibleEmployees(employee);
                carRepairAppointmentRepository.save(a);
            }
        }
        carServiceEmployeeRepository.deleteById(id);
    }
}
