package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.Car;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Car entity.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    default Optional<Car> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Car> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Car> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select car from Car car left join fetch car.carRepairAppointment", countQuery = "select count(car) from Car car")
    Page<Car> findAllWithToOneRelationships(Pageable pageable);

    @Query("select car from Car car left join fetch car.carRepairAppointment")
    List<Car> findAllWithToOneRelationships();

    @Query("select car from Car car left join fetch car.carRepairAppointment where car.id =:id")
    Optional<Car> findOneWithToOneRelationships(@Param("id") Long id);
}
