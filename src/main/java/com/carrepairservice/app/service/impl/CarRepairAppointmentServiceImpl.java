package com.carrepairservice.app.service.impl;

import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.repository.CarRepairAppointmentRepository;
import com.carrepairservice.app.repository.CarRepository;
import com.carrepairservice.app.repository.CarServiceEmployeeRepository;
import com.carrepairservice.app.repository.CarServiceRepository;
import com.carrepairservice.app.service.CarRepairAppointmentService;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import com.carrepairservice.app.service.mapper.CarRepairAppointmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.carrepairservice.app.domain.CarRepairAppointment}.
 */
@Service
@Transactional
public class CarRepairAppointmentServiceImpl implements CarRepairAppointmentService {

    private final Logger log = LoggerFactory.getLogger(CarRepairAppointmentServiceImpl.class);

    private final CarRepairAppointmentRepository carRepairAppointmentRepository;

    private final CarRepairAppointmentMapper carRepairAppointmentMapper;

    private final CarRepository carRepository;

    private final CarServiceEmployeeRepository carServiceEmployeeRepository;

    private final CarServiceRepository carServiceRepository;

    public CarRepairAppointmentServiceImpl(
        CarRepairAppointmentRepository carRepairAppointmentRepository,
        CarRepairAppointmentMapper carRepairAppointmentMapper,
        CarRepository carRepository,
        CarServiceEmployeeRepository carServiceEmployeeRepository,
        CarServiceRepository carServiceRepository
    ) {
        this.carRepairAppointmentRepository = carRepairAppointmentRepository;
        this.carRepairAppointmentMapper = carRepairAppointmentMapper;
        this.carRepository = carRepository;
        this.carServiceEmployeeRepository = carServiceEmployeeRepository;
        this.carServiceRepository = carServiceRepository;
    }

    @Override
    public CarRepairAppointmentDTO save(CarRepairAppointmentDTO carRepairAppointmentDTO) {
        log.debug("Request to save CarRepairAppointment : {}", carRepairAppointmentDTO);
        CarRepairAppointment carRepairAppointment = carRepairAppointmentMapper.toEntity(carRepairAppointmentDTO);
        carRepairAppointment = carRepairAppointmentRepository.save(carRepairAppointment);

        var car = carRepairAppointment.getCar();
        car.setCarRepairAppointment(carRepairAppointment);
        carRepository.save(car);

        var employees = carRepairAppointment.getResponsibleEmployees();
        for (var e : employees) {
            e.getRepairAppointments().add(carRepairAppointment);
            carServiceEmployeeRepository.save(e);
        }
        for (var e : carServiceEmployeeRepository.findAll()) {
            if (!employees.contains(e)) {
                e.removeRepairAppointments(carRepairAppointment);
                carServiceEmployeeRepository.save(e);
            }
        }

        var service = carRepairAppointment.getCarService();

        for (CarService s : carServiceRepository.findAll()) {
            if (!s.getAddress().equals(service.getAddress())) {
                s.removeRepairAppointments(carRepairAppointment);
                carServiceRepository.save(s);
            }
        }

        service.addRepairAppointments(carRepairAppointment);
        carServiceRepository.save(service);

        return carRepairAppointmentMapper.toDto(carRepairAppointment);
    }

    @Override
    public CarRepairAppointmentDTO update(CarRepairAppointmentDTO carRepairAppointmentDTO) {
        log.debug("Request to update CarRepairAppointment : {}", carRepairAppointmentDTO);
        CarRepairAppointment carRepairAppointment = carRepairAppointmentMapper.toEntity(carRepairAppointmentDTO);
        carRepairAppointment = carRepairAppointmentRepository.save(carRepairAppointment);

        var car = carRepairAppointment.getCar();
        car.setCarRepairAppointment(carRepairAppointment);
        carRepository.save(car);

        var employees = carRepairAppointment.getResponsibleEmployees();
        for (var e : employees) {
            e.getRepairAppointments().add(carRepairAppointment);
            carServiceEmployeeRepository.save(e);
        }
        for (var e : carServiceEmployeeRepository.findAll()) {
            if (!employees.contains(e)) {
                e.removeRepairAppointments(carRepairAppointment);
                carServiceEmployeeRepository.save(e);
            }
        }

        var service = carRepairAppointment.getCarService();

        for (CarService s : carServiceRepository.findAll()) {
            if (!s.getAddress().equals(service.getAddress())) {
                s.removeRepairAppointments(carRepairAppointment);
                carServiceRepository.save(s);
            }
        }

        service.addRepairAppointments(carRepairAppointment);
        carServiceRepository.save(service);

        return carRepairAppointmentMapper.toDto(carRepairAppointment);
    }

    @Override
    public Optional<CarRepairAppointmentDTO> partialUpdate(CarRepairAppointmentDTO carRepairAppointmentDTO) {
        log.debug("Request to partially update CarRepairAppointment : {}", carRepairAppointmentDTO);

        return carRepairAppointmentRepository
            .findById(carRepairAppointmentDTO.getId())
            .map(existingCarRepairAppointment -> {
                carRepairAppointmentMapper.partialUpdate(existingCarRepairAppointment, carRepairAppointmentDTO);

                return existingCarRepairAppointment;
            })
            .map(carRepairAppointmentRepository::save)
            .map(carRepairAppointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarRepairAppointmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarRepairAppointments");
        return carRepairAppointmentRepository.findAll(pageable).map(carRepairAppointmentMapper::toDto);
    }

    public Page<CarRepairAppointmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return carRepairAppointmentRepository.findAllWithEagerRelationships(pageable).map(carRepairAppointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarRepairAppointmentDTO> findOne(Long id) {
        log.debug("Request to get CarRepairAppointment : {}", id);
        return carRepairAppointmentRepository.findOneWithEagerRelationships(id).map(carRepairAppointmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarRepairAppointment : {}", id);
        var app = carRepairAppointmentRepository.findById(id);

        if (app.isPresent()) {
            var appointment = app.get();

            var car = appointment.getCar();
            car.setCarRepairAppointment(null);
            carRepository.save(car);

            var carService = appointment.getCarService();
            carService.removeRepairAppointments(appointment);
            carServiceRepository.save(carService);

            for (var e : appointment.getResponsibleEmployees()) {
                e.removeRepairAppointments(appointment);
                carServiceEmployeeRepository.save(e);
            }
        }

        carRepairAppointmentRepository.deleteById(id);
    }
}
