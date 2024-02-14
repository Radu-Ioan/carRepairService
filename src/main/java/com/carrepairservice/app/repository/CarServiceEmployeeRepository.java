package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarServiceEmployee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarServiceEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarServiceEmployeeRepository
    extends JpaRepository<CarServiceEmployee, Long>, JpaSpecificationExecutor<CarServiceEmployee> {}
