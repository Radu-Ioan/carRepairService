package com.carrepairservice.app.service;

import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.carrepairservice.app.domain.CarServiceEmployee}.
 */
public interface CarServiceEmployeeService {
    /**
     * Save a carServiceEmployee.
     *
     * @param carServiceEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    CarServiceEmployeeDTO save(CarServiceEmployeeDTO carServiceEmployeeDTO);

    /**
     * Updates a carServiceEmployee.
     *
     * @param carServiceEmployeeDTO the entity to update.
     * @return the persisted entity.
     */
    CarServiceEmployeeDTO update(CarServiceEmployeeDTO carServiceEmployeeDTO);

    /**
     * Partially updates a carServiceEmployee.
     *
     * @param carServiceEmployeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarServiceEmployeeDTO> partialUpdate(CarServiceEmployeeDTO carServiceEmployeeDTO);

    /**
     * Get all the carServiceEmployees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarServiceEmployeeDTO> findAll(Pageable pageable);

    /**
     * Get all the carServiceEmployees with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarServiceEmployeeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" carServiceEmployee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarServiceEmployeeDTO> findOne(Long id);

    /**
     * Delete the "id" carServiceEmployee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
