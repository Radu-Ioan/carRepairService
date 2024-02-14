package com.carrepairservice.app.service;

import com.carrepairservice.app.domain.*; // for static metamodels
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.repository.CarRepairAppointmentRepository;
import com.carrepairservice.app.service.criteria.CarRepairAppointmentCriteria;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import com.carrepairservice.app.service.mapper.CarRepairAppointmentMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CarRepairAppointment} entities in the database.
 * The main input is a {@link CarRepairAppointmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarRepairAppointmentDTO} or a {@link Page} of {@link CarRepairAppointmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarRepairAppointmentQueryService extends QueryService<CarRepairAppointment> {

    private final Logger log = LoggerFactory.getLogger(CarRepairAppointmentQueryService.class);

    private final CarRepairAppointmentRepository carRepairAppointmentRepository;

    private final CarRepairAppointmentMapper carRepairAppointmentMapper;

    public CarRepairAppointmentQueryService(
        CarRepairAppointmentRepository carRepairAppointmentRepository,
        CarRepairAppointmentMapper carRepairAppointmentMapper
    ) {
        this.carRepairAppointmentRepository = carRepairAppointmentRepository;
        this.carRepairAppointmentMapper = carRepairAppointmentMapper;
    }

    /**
     * Return a {@link List} of {@link CarRepairAppointmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarRepairAppointmentDTO> findByCriteria(CarRepairAppointmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CarRepairAppointment> specification = createSpecification(criteria);
        return carRepairAppointmentMapper.toDto(carRepairAppointmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CarRepairAppointmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarRepairAppointmentDTO> findByCriteria(CarRepairAppointmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CarRepairAppointment> specification = createSpecification(criteria);
        return carRepairAppointmentRepository.findAll(specification, page).map(carRepairAppointmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarRepairAppointmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CarRepairAppointment> specification = createSpecification(criteria);
        return carRepairAppointmentRepository.count(specification);
    }

    /**
     * Function to convert {@link CarRepairAppointmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CarRepairAppointment> createSpecification(CarRepairAppointmentCriteria criteria) {
        Specification<CarRepairAppointment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CarRepairAppointment_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), CarRepairAppointment_.date));
            }
        }
        return specification;
    }
}
