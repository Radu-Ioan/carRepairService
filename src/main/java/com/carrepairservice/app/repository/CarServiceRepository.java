package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarServiceRepository extends JpaRepository<CarService, Long>, JpaSpecificationExecutor<CarService> {}
