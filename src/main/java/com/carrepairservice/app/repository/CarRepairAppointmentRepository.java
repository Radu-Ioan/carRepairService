package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarRepairAppointment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarRepairAppointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarRepairAppointmentRepository
    extends JpaRepository<CarRepairAppointment, Long>, JpaSpecificationExecutor<CarRepairAppointment> {}
