package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarServiceRepository extends JpaRepository<CarService, Long>, JpaSpecificationExecutor<CarService> {}
