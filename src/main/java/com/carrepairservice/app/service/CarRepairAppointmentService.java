package com.carrepairservice.app.service;

import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.carrepairservice.app.domain.CarRepairAppointment}.
 */
public interface CarRepairAppointmentService {
    /**
     * Save a carRepairAppointment.
     *
     * @param carRepairAppointmentDTO the entity to save.
     * @return the persisted entity.
     */
    CarRepairAppointmentDTO save(CarRepairAppointmentDTO carRepairAppointmentDTO);

    /**
     * Updates a carRepairAppointment.
     *
     * @param carRepairAppointmentDTO the entity to update.
     * @return the persisted entity.
     */
    CarRepairAppointmentDTO update(CarRepairAppointmentDTO carRepairAppointmentDTO);

    /**
     * Partially updates a carRepairAppointment.
     *
     * @param carRepairAppointmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarRepairAppointmentDTO> partialUpdate(CarRepairAppointmentDTO carRepairAppointmentDTO);

    /**
     * Get all the carRepairAppointments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarRepairAppointmentDTO> findAll(Pageable pageable);

    /**
     * Get all the carRepairAppointments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarRepairAppointmentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" carRepairAppointment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarRepairAppointmentDTO> findOne(Long id);

    /**
     * Delete the "id" carRepairAppointment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
