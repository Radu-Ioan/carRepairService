package com.carrepairservice.app.service;

import com.carrepairservice.app.domain.*; // for static metamodels
import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.repository.CarServiceEmployeeRepository;
import com.carrepairservice.app.service.criteria.CarServiceEmployeeCriteria;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import com.carrepairservice.app.service.mapper.CarServiceEmployeeMapper;
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
 * Service for executing complex queries for {@link CarServiceEmployee} entities in the database.
 * The main input is a {@link CarServiceEmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarServiceEmployeeDTO} or a {@link Page} of {@link CarServiceEmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarServiceEmployeeQueryService extends QueryService<CarServiceEmployee> {

    private final Logger log = LoggerFactory.getLogger(CarServiceEmployeeQueryService.class);

    private final CarServiceEmployeeRepository carServiceEmployeeRepository;

    private final CarServiceEmployeeMapper carServiceEmployeeMapper;

    public CarServiceEmployeeQueryService(
        CarServiceEmployeeRepository carServiceEmployeeRepository,
        CarServiceEmployeeMapper carServiceEmployeeMapper
    ) {
        this.carServiceEmployeeRepository = carServiceEmployeeRepository;
        this.carServiceEmployeeMapper = carServiceEmployeeMapper;
    }

    /**
     * Return a {@link List} of {@link CarServiceEmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarServiceEmployeeDTO> findByCriteria(CarServiceEmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CarServiceEmployee> specification = createSpecification(criteria);
        return carServiceEmployeeMapper.toDto(carServiceEmployeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CarServiceEmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarServiceEmployeeDTO> findByCriteria(CarServiceEmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CarServiceEmployee> specification = createSpecification(criteria);
        return carServiceEmployeeRepository.findAll(specification, page).map(carServiceEmployeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarServiceEmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CarServiceEmployee> specification = createSpecification(criteria);
        return carServiceEmployeeRepository.count(specification);
    }

    /**
     * Function to convert {@link CarServiceEmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CarServiceEmployee> createSpecification(CarServiceEmployeeCriteria criteria) {
        Specification<CarServiceEmployee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CarServiceEmployee_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CarServiceEmployee_.name));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), CarServiceEmployee_.age));
            }
            if (criteria.getYearsOfExperience() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getYearsOfExperience(), CarServiceEmployee_.yearsOfExperience));
            }
        }
        return specification;
    }
}
