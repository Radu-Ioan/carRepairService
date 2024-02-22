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
public interface CarServiceRepository extends JpaRepository<CarService, Long>, JpaSpecificationExecutor<CarService> {
    default List<CarService> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Optional<CarService> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default Page<CarService> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select distinct carservice from CarService carservice left join fetch carservice.repairAppointments")
    List<CarService> findAllWithToOneRelationships();

    @Query("select carservice from CarService carservice left join fetch carservice.repairAppointments where carservice.id =:id")
    Optional<CarService> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "select distinct carservice from CarService carservice left join fetch carservice.repairAppointments",
        countQuery = "select count(distinct carservice) from CarService carservice"
    )
    Page<CarService> findAllWithToOneRelationships(Pageable pageable);
}
