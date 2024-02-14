package com.carrepairservice.app.web.rest;

import com.carrepairservice.app.repository.CarRepairAppointmentRepository;
import com.carrepairservice.app.service.CarRepairAppointmentQueryService;
import com.carrepairservice.app.service.CarRepairAppointmentService;
import com.carrepairservice.app.service.criteria.CarRepairAppointmentCriteria;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.carrepairservice.app.domain.CarRepairAppointment}.
 */
@RestController
@RequestMapping("/api/car-repair-appointments")
public class CarRepairAppointmentResource {

    private final Logger log = LoggerFactory.getLogger(CarRepairAppointmentResource.class);

    private static final String ENTITY_NAME = "carRepairAppointment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarRepairAppointmentService carRepairAppointmentService;

    private final CarRepairAppointmentRepository carRepairAppointmentRepository;

    private final CarRepairAppointmentQueryService carRepairAppointmentQueryService;

    public CarRepairAppointmentResource(
        CarRepairAppointmentService carRepairAppointmentService,
        CarRepairAppointmentRepository carRepairAppointmentRepository,
        CarRepairAppointmentQueryService carRepairAppointmentQueryService
    ) {
        this.carRepairAppointmentService = carRepairAppointmentService;
        this.carRepairAppointmentRepository = carRepairAppointmentRepository;
        this.carRepairAppointmentQueryService = carRepairAppointmentQueryService;
    }

    /**
     * {@code POST  /car-repair-appointments} : Create a new carRepairAppointment.
     *
     * @param carRepairAppointmentDTO the carRepairAppointmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carRepairAppointmentDTO, or with status {@code 400 (Bad Request)} if the carRepairAppointment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarRepairAppointmentDTO> createCarRepairAppointment(
        @Valid @RequestBody CarRepairAppointmentDTO carRepairAppointmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CarRepairAppointment : {}", carRepairAppointmentDTO);
        if (carRepairAppointmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new carRepairAppointment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarRepairAppointmentDTO result = carRepairAppointmentService.save(carRepairAppointmentDTO);
        return ResponseEntity
            .created(new URI("/api/car-repair-appointments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /car-repair-appointments/:id} : Updates an existing carRepairAppointment.
     *
     * @param id the id of the carRepairAppointmentDTO to save.
     * @param carRepairAppointmentDTO the carRepairAppointmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carRepairAppointmentDTO,
     * or with status {@code 400 (Bad Request)} if the carRepairAppointmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carRepairAppointmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarRepairAppointmentDTO> updateCarRepairAppointment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarRepairAppointmentDTO carRepairAppointmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CarRepairAppointment : {}, {}", id, carRepairAppointmentDTO);
        if (carRepairAppointmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carRepairAppointmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepairAppointmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarRepairAppointmentDTO result = carRepairAppointmentService.update(carRepairAppointmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carRepairAppointmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /car-repair-appointments/:id} : Partial updates given fields of an existing carRepairAppointment, field will ignore if it is null
     *
     * @param id the id of the carRepairAppointmentDTO to save.
     * @param carRepairAppointmentDTO the carRepairAppointmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carRepairAppointmentDTO,
     * or with status {@code 400 (Bad Request)} if the carRepairAppointmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carRepairAppointmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carRepairAppointmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarRepairAppointmentDTO> partialUpdateCarRepairAppointment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarRepairAppointmentDTO carRepairAppointmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarRepairAppointment partially : {}, {}", id, carRepairAppointmentDTO);
        if (carRepairAppointmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carRepairAppointmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepairAppointmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarRepairAppointmentDTO> result = carRepairAppointmentService.partialUpdate(carRepairAppointmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carRepairAppointmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /car-repair-appointments} : get all the carRepairAppointments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carRepairAppointments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarRepairAppointmentDTO>> getAllCarRepairAppointments(
        CarRepairAppointmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CarRepairAppointments by criteria: {}", criteria);

        Page<CarRepairAppointmentDTO> page = carRepairAppointmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-repair-appointments/count} : count all the carRepairAppointments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCarRepairAppointments(CarRepairAppointmentCriteria criteria) {
        log.debug("REST request to count CarRepairAppointments by criteria: {}", criteria);
        return ResponseEntity.ok().body(carRepairAppointmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /car-repair-appointments/:id} : get the "id" carRepairAppointment.
     *
     * @param id the id of the carRepairAppointmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carRepairAppointmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarRepairAppointmentDTO> getCarRepairAppointment(@PathVariable("id") Long id) {
        log.debug("REST request to get CarRepairAppointment : {}", id);
        Optional<CarRepairAppointmentDTO> carRepairAppointmentDTO = carRepairAppointmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carRepairAppointmentDTO);
    }

    /**
     * {@code DELETE  /car-repair-appointments/:id} : delete the "id" carRepairAppointment.
     *
     * @param id the id of the carRepairAppointmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarRepairAppointment(@PathVariable("id") Long id) {
        log.debug("REST request to delete CarRepairAppointment : {}", id);
        carRepairAppointmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
