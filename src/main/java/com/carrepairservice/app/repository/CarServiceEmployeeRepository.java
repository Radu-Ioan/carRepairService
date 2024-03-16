package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarServiceEmployee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarServiceEmployee entity.
 */
@Repository
public interface CarServiceEmployeeRepository
    extends JpaRepository<CarServiceEmployee, Long>, JpaSpecificationExecutor<CarServiceEmployee> {
    default Optional<CarServiceEmployee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CarServiceEmployee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CarServiceEmployee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select carServiceEmployee from CarServiceEmployee carServiceEmployee left join fetch carServiceEmployee.carService",
        countQuery = "select count(carServiceEmployee) from CarServiceEmployee carServiceEmployee"
    )
    Page<CarServiceEmployee> findAllWithToOneRelationships(Pageable pageable);

    @Query("select carServiceEmployee from CarServiceEmployee carServiceEmployee left join fetch carServiceEmployee.carService")
    List<CarServiceEmployee> findAllWithToOneRelationships();

    @Query(
        "select carServiceEmployee from CarServiceEmployee carServiceEmployee left join fetch carServiceEmployee.carService where carServiceEmployee.id =:id"
    )
    Optional<CarServiceEmployee> findOneWithToOneRelationships(@Param("id") Long id);
}
