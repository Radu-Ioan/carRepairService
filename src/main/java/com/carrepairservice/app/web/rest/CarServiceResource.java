package com.carrepairservice.app.web.rest;

import com.carrepairservice.app.repository.CarServiceRepository;
import com.carrepairservice.app.service.CarServiceQueryService;
import com.carrepairservice.app.service.CarServiceService;
import com.carrepairservice.app.service.criteria.CarServiceCriteria;
import com.carrepairservice.app.service.dto.CarServiceDTO;
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
 * REST controller for managing {@link com.carrepairservice.app.domain.CarService}.
 */
@RestController
@RequestMapping("/api/car-services")
public class CarServiceResource {

    private final Logger log = LoggerFactory.getLogger(CarServiceResource.class);

    private static final String ENTITY_NAME = "carService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarServiceService carServiceService;

    private final CarServiceRepository carServiceRepository;

    private final CarServiceQueryService carServiceQueryService;

    public CarServiceResource(
        CarServiceService carServiceService,
        CarServiceRepository carServiceRepository,
        CarServiceQueryService carServiceQueryService
    ) {
        this.carServiceService = carServiceService;
        this.carServiceRepository = carServiceRepository;
        this.carServiceQueryService = carServiceQueryService;
    }

    /**
     * {@code POST  /car-services} : Create a new carService.
     *
     * @param carServiceDTO the carServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carServiceDTO, or with status {@code 400 (Bad Request)} if the carService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarServiceDTO> createCarService(@Valid @RequestBody CarServiceDTO carServiceDTO) throws URISyntaxException {
        log.debug("REST request to save CarService : {}", carServiceDTO);
        if (carServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new carService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarServiceDTO result = carServiceService.save(carServiceDTO);
        return ResponseEntity
            .created(new URI("/api/car-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /car-services/:id} : Updates an existing carService.
     *
     * @param id the id of the carServiceDTO to save.
     * @param carServiceDTO the carServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carServiceDTO,
     * or with status {@code 400 (Bad Request)} if the carServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarServiceDTO> updateCarService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarServiceDTO carServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CarService : {}, {}", id, carServiceDTO);
        if (carServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarServiceDTO result = carServiceService.update(carServiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carServiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /car-services/:id} : Partial updates given fields of an existing carService, field will ignore if it is null
     *
     * @param id the id of the carServiceDTO to save.
     * @param carServiceDTO the carServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carServiceDTO,
     * or with status {@code 400 (Bad Request)} if the carServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarServiceDTO> partialUpdateCarService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarServiceDTO carServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarService partially : {}, {}", id, carServiceDTO);
        if (carServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarServiceDTO> result = carServiceService.partialUpdate(carServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /car-services} : get all the carServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarServiceDTO>> getAllCarServices(
        CarServiceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CarServices by criteria: {}", criteria);

        Page<CarServiceDTO> page = carServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-services/count} : count all the carServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCarServices(CarServiceCriteria criteria) {
        log.debug("REST request to count CarServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(carServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /car-services/:id} : get the "id" carService.
     *
     * @param id the id of the carServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarServiceDTO> getCarService(@PathVariable("id") Long id) {
        log.debug("REST request to get CarService : {}", id);
        Optional<CarServiceDTO> carServiceDTO = carServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carServiceDTO);
    }

    /**
     * {@code DELETE  /car-services/:id} : delete the "id" carService.
     *
     * @param id the id of the carServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarService(@PathVariable("id") Long id) {
        log.debug("REST request to delete CarService : {}", id);
        carServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
