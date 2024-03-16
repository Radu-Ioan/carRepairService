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
 *
 * When extending this class, extend CarServiceEmployeeRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CarServiceEmployeeRepository
    extends
        CarServiceEmployeeRepositoryWithBagRelationships,
        JpaRepository<CarServiceEmployee, Long>,
        JpaSpecificationExecutor<CarServiceEmployee> {
    default Optional<CarServiceEmployee> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<CarServiceEmployee> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<CarServiceEmployee> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
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
