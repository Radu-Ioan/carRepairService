package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarServiceEmployee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CarServiceEmployeeRepositoryWithBagRelationships {
    Optional<CarServiceEmployee> fetchBagRelationships(Optional<CarServiceEmployee> carServiceEmployee);

    List<CarServiceEmployee> fetchBagRelationships(List<CarServiceEmployee> carServiceEmployees);

    Page<CarServiceEmployee> fetchBagRelationships(Page<CarServiceEmployee> carServiceEmployees);
}
