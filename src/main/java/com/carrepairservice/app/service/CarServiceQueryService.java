package com.carrepairservice.app.service;

import com.carrepairservice.app.domain.*; // for static metamodels
import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.repository.CarServiceRepository;
import com.carrepairservice.app.service.criteria.CarServiceCriteria;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import com.carrepairservice.app.service.mapper.CarServiceMapper;
import jakarta.persistence.criteria.JoinType;
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
 * Service for executing complex queries for {@link CarService} entities in the database.
 * The main input is a {@link CarServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarServiceDTO} or a {@link Page} of {@link CarServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarServiceQueryService extends QueryService<CarService> {

    private final Logger log = LoggerFactory.getLogger(CarServiceQueryService.class);

    private final CarServiceRepository carServiceRepository;

    private final CarServiceMapper carServiceMapper;

    public CarServiceQueryService(CarServiceRepository carServiceRepository, CarServiceMapper carServiceMapper) {
        this.carServiceRepository = carServiceRepository;
        this.carServiceMapper = carServiceMapper;
    }

    /**
     * Return a {@link List} of {@link CarServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarServiceDTO> findByCriteria(CarServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CarService> specification = createSpecification(criteria);
        return carServiceMapper.toDto(carServiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CarServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarServiceDTO> findByCriteria(CarServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CarService> specification = createSpecification(criteria);
        return carServiceRepository.findAll(specification, page).map(carServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CarService> specification = createSpecification(criteria);
        return carServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link CarServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CarService> createSpecification(CarServiceCriteria criteria) {
        Specification<CarService> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CarService_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CarService_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), CarService_.address));
            }
            if (criteria.getRepairAppointmentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRepairAppointmentsId(),
                            root -> root.join(CarService_.repairAppointments, JoinType.LEFT).get(CarRepairAppointment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
