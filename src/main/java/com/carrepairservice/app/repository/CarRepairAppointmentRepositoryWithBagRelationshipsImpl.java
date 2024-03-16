package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarRepairAppointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CarRepairAppointmentRepositoryWithBagRelationshipsImpl implements CarRepairAppointmentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CarRepairAppointment> fetchBagRelationships(Optional<CarRepairAppointment> carRepairAppointment) {
        return carRepairAppointment.map(this::fetchResponsibleEmployees);
    }

    @Override
    public Page<CarRepairAppointment> fetchBagRelationships(Page<CarRepairAppointment> carRepairAppointments) {
        return new PageImpl<>(
            fetchBagRelationships(carRepairAppointments.getContent()),
            carRepairAppointments.getPageable(),
            carRepairAppointments.getTotalElements()
        );
    }

    @Override
    public List<CarRepairAppointment> fetchBagRelationships(List<CarRepairAppointment> carRepairAppointments) {
        return Optional.of(carRepairAppointments).map(this::fetchResponsibleEmployees).orElse(Collections.emptyList());
    }

    CarRepairAppointment fetchResponsibleEmployees(CarRepairAppointment result) {
        return entityManager
            .createQuery(
                "select carRepairAppointment from CarRepairAppointment carRepairAppointment left join fetch carRepairAppointment.responsibleEmployees where carRepairAppointment.id = :id",
                CarRepairAppointment.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<CarRepairAppointment> fetchResponsibleEmployees(List<CarRepairAppointment> carRepairAppointments) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, carRepairAppointments.size()).forEach(index -> order.put(carRepairAppointments.get(index).getId(), index));
        List<CarRepairAppointment> result = entityManager
            .createQuery(
                "select carRepairAppointment from CarRepairAppointment carRepairAppointment left join fetch carRepairAppointment.responsibleEmployees where carRepairAppointment in :carRepairAppointments",
                CarRepairAppointment.class
            )
            .setParameter("carRepairAppointments", carRepairAppointments)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
