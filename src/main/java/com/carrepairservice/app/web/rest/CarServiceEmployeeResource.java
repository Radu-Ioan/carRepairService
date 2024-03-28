package com.carrepairservice.app.web.rest;

import com.carrepairservice.app.repository.CarServiceEmployeeRepository;
import com.carrepairservice.app.security.AuthoritiesConstants;
import com.carrepairservice.app.service.CarServiceEmployeeQueryService;
import com.carrepairservice.app.service.CarServiceEmployeeService;
import com.carrepairservice.app.service.criteria.CarServiceEmployeeCriteria;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import com.carrepairservice.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.carrepairservice.app.domain.CarServiceEmployee}.
 */
@RestController
@RequestMapping("/api/car-service-employees")
public class CarServiceEmployeeResource {

    private final Logger log = LoggerFactory.getLogger(CarServiceEmployeeResource.class);

    private static final String ENTITY_NAME = "carServiceEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarServiceEmployeeService carServiceEmployeeService;

    private final CarServiceEmployeeRepository carServiceEmployeeRepository;

    private final CarServiceEmployeeQueryService carServiceEmployeeQueryService;

    public CarServiceEmployeeResource(
        CarServiceEmployeeService carServiceEmployeeService,
        CarServiceEmployeeRepository carServiceEmployeeRepository,
        CarServiceEmployeeQueryService carServiceEmployeeQueryService
    ) {
        this.carServiceEmployeeService = carServiceEmployeeService;
        this.carServiceEmployeeRepository = carServiceEmployeeRepository;
        this.carServiceEmployeeQueryService = carServiceEmployeeQueryService;
    }

    /**
     * {@code POST  /car-service-employees} : Create a new carServiceEmployee.
     *
     * @param carServiceEmployeeDTO the carServiceEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carServiceEmployeeDTO, or with status {@code 400 (Bad Request)} if the carServiceEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarServiceEmployeeDTO> createCarServiceEmployee(@Valid @RequestBody CarServiceEmployeeDTO carServiceEmployeeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CarServiceEmployee : {}", carServiceEmployeeDTO);
        if (carServiceEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new carServiceEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarServiceEmployeeDTO result = carServiceEmployeeService.save(carServiceEmployeeDTO);
        return ResponseEntity
            .created(new URI("/api/car-service-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /car-service-employees/:id} : Updates an existing carServiceEmployee.
     *
     * @param id the id of the carServiceEmployeeDTO to save.
     * @param carServiceEmployeeDTO the carServiceEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carServiceEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the carServiceEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carServiceEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarServiceEmployeeDTO> updateCarServiceEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarServiceEmployeeDTO carServiceEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CarServiceEmployee : {}, {}", id, carServiceEmployeeDTO);
        if (carServiceEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carServiceEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carServiceEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarServiceEmployeeDTO result = carServiceEmployeeService.update(carServiceEmployeeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carServiceEmployeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /car-service-employees/:id} : Partial updates given fields of an existing carServiceEmployee, field will ignore if it is null
     *
     * @param id the id of the carServiceEmployeeDTO to save.
     * @param carServiceEmployeeDTO the carServiceEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carServiceEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the carServiceEmployeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carServiceEmployeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carServiceEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarServiceEmployeeDTO> partialUpdateCarServiceEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarServiceEmployeeDTO carServiceEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarServiceEmployee partially : {}, {}", id, carServiceEmployeeDTO);
        if (carServiceEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carServiceEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carServiceEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarServiceEmployeeDTO> result = carServiceEmployeeService.partialUpdate(carServiceEmployeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carServiceEmployeeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /car-service-employees} : get all the carServiceEmployees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carServiceEmployees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarServiceEmployeeDTO>> getAllCarServiceEmployees(
        CarServiceEmployeeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CarServiceEmployees by criteria: {}", criteria);

        Page<CarServiceEmployeeDTO> page = carServiceEmployeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-service-employees/count} : count all the carServiceEmployees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCarServiceEmployees(CarServiceEmployeeCriteria criteria) {
        log.debug("REST request to count CarServiceEmployees by criteria: {}", criteria);
        return ResponseEntity.ok().body(carServiceEmployeeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /car-service-employees/:id} : get the "id" carServiceEmployee.
     *
     * @param id the id of the carServiceEmployeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carServiceEmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarServiceEmployeeDTO> getCarServiceEmployee(@PathVariable("id") Long id) {
        log.debug("REST request to get CarServiceEmployee : {}", id);
        Optional<CarServiceEmployeeDTO> carServiceEmployeeDTO = carServiceEmployeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carServiceEmployeeDTO);
    }

    /**
     * {@code DELETE  /car-service-employees/:id} : delete the "id" carServiceEmployee.
     *
     * @param id the id of the carServiceEmployeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteCarServiceEmployee(@PathVariable("id") Long id) {
        log.debug("REST request to delete CarServiceEmployee : {}", id);
        carServiceEmployeeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
