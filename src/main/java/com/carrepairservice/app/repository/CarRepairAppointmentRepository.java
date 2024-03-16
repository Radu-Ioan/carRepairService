package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarRepairAppointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarRepairAppointment entity.
 *
 * When extending this class, extend CarRepairAppointmentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CarRepairAppointmentRepository
    extends
        CarRepairAppointmentRepositoryWithBagRelationships,
        JpaRepository<CarRepairAppointment, Long>,
        JpaSpecificationExecutor<CarRepairAppointment> {
    default Optional<CarRepairAppointment> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<CarRepairAppointment> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<CarRepairAppointment> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select carRepairAppointment from CarRepairAppointment carRepairAppointment left join fetch carRepairAppointment.car left join fetch carRepairAppointment.carService",
        countQuery = "select count(carRepairAppointment) from CarRepairAppointment carRepairAppointment"
    )
    Page<CarRepairAppointment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select carRepairAppointment from CarRepairAppointment carRepairAppointment left join fetch carRepairAppointment.car left join fetch carRepairAppointment.carService"
    )
    List<CarRepairAppointment> findAllWithToOneRelationships();

    @Query(
        "select carRepairAppointment from CarRepairAppointment carRepairAppointment left join fetch carRepairAppointment.car left join fetch carRepairAppointment.carService where carRepairAppointment.id =:id"
    )
    Optional<CarRepairAppointment> findOneWithToOneRelationships(@Param("id") Long id);
}
