package com.carrepairservice.app.service;

import com.carrepairservice.app.service.dto.CarServiceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.carrepairservice.app.domain.CarService}.
 */
public interface CarServiceService {
    /**
     * Save a carService.
     *
     * @param carServiceDTO the entity to save.
     * @return the persisted entity.
     */
    CarServiceDTO save(CarServiceDTO carServiceDTO);

    /**
     * Updates a carService.
     *
     * @param carServiceDTO the entity to update.
     * @return the persisted entity.
     */
    CarServiceDTO update(CarServiceDTO carServiceDTO);

    /**
     * Partially updates a carService.
     *
     * @param carServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarServiceDTO> partialUpdate(CarServiceDTO carServiceDTO);

    /**
     * Get all the carServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarServiceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" carService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarServiceDTO> findOne(Long id);

    /**
     * Delete the "id" carService.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
