package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarRepairAppointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CarRepairAppointmentRepositoryWithBagRelationships {
    Optional<CarRepairAppointment> fetchBagRelationships(Optional<CarRepairAppointment> carRepairAppointment);

    List<CarRepairAppointment> fetchBagRelationships(List<CarRepairAppointment> carRepairAppointments);

    Page<CarRepairAppointment> fetchBagRelationships(Page<CarRepairAppointment> carRepairAppointments);
}
