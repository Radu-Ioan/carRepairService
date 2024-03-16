package com.carrepairservice.app.repository;

import com.carrepairservice.app.domain.CarServiceEmployee;
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
public class CarServiceEmployeeRepositoryWithBagRelationshipsImpl implements CarServiceEmployeeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CarServiceEmployee> fetchBagRelationships(Optional<CarServiceEmployee> carServiceEmployee) {
        return carServiceEmployee.map(this::fetchRepairAppointments);
    }

    @Override
    public Page<CarServiceEmployee> fetchBagRelationships(Page<CarServiceEmployee> carServiceEmployees) {
        return new PageImpl<>(
            fetchBagRelationships(carServiceEmployees.getContent()),
            carServiceEmployees.getPageable(),
            carServiceEmployees.getTotalElements()
        );
    }

    @Override
    public List<CarServiceEmployee> fetchBagRelationships(List<CarServiceEmployee> carServiceEmployees) {
        return Optional.of(carServiceEmployees).map(this::fetchRepairAppointments).orElse(Collections.emptyList());
    }

    CarServiceEmployee fetchRepairAppointments(CarServiceEmployee result) {
        return entityManager
            .createQuery(
                "select carServiceEmployee from CarServiceEmployee carServiceEmployee left join fetch carServiceEmployee.repairAppointments where carServiceEmployee.id = :id",
                CarServiceEmployee.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<CarServiceEmployee> fetchRepairAppointments(List<CarServiceEmployee> carServiceEmployees) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, carServiceEmployees.size()).forEach(index -> order.put(carServiceEmployees.get(index).getId(), index));
        List<CarServiceEmployee> result = entityManager
            .createQuery(
                "select carServiceEmployee from CarServiceEmployee carServiceEmployee left join fetch carServiceEmployee.repairAppointments where carServiceEmployee in :carServiceEmployees",
                CarServiceEmployee.class
            )
            .setParameter("carServiceEmployees", carServiceEmployees)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
