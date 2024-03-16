package com.carrepairservice.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carrepairservice.app.IntegrationTest;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.repository.CarServiceRepository;
import com.carrepairservice.app.service.dto.CarServiceDTO;
import com.carrepairservice.app.service.mapper.CarServiceMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarServiceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/car-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarServiceRepository carServiceRepository;

    @Autowired
    private CarServiceMapper carServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarServiceMockMvc;

    private CarService carService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarService createEntity(EntityManager em) {
        CarService carService = new CarService().name(DEFAULT_NAME).address(DEFAULT_ADDRESS);
        return carService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarService createUpdatedEntity(EntityManager em) {
        CarService carService = new CarService().name(UPDATED_NAME).address(UPDATED_ADDRESS);
        return carService;
    }

    @BeforeEach
    public void initTest() {
        carService = createEntity(em);
    }

    @Test
    @Transactional
    void createCarService() throws Exception {
        int databaseSizeBeforeCreate = carServiceRepository.findAll().size();
        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);
        restCarServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carServiceDTO)))
            .andExpect(status().isCreated());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeCreate + 1);
        CarService testCarService = carServiceList.get(carServiceList.size() - 1);
        assertThat(testCarService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarService.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createCarServiceWithExistingId() throws Exception {
        // Create the CarService with an existing ID
        carService.setId(1L);
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        int databaseSizeBeforeCreate = carServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = carServiceRepository.findAll().size();
        // set the field null
        carService.setAddress(null);

        // Create the CarService, which fails.
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        restCarServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carServiceDTO)))
            .andExpect(status().isBadRequest());

        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarServices() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList
        restCarServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getCarService() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get the carService
        restCarServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, carService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carService.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getCarServicesByIdFiltering() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        Long id = carService.getId();

        defaultCarServiceShouldBeFound("id.equals=" + id);
        defaultCarServiceShouldNotBeFound("id.notEquals=" + id);

        defaultCarServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultCarServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarServiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarServicesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where name equals to DEFAULT_NAME
        defaultCarServiceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the carServiceList where name equals to UPDATED_NAME
        defaultCarServiceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServicesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCarServiceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the carServiceList where name equals to UPDATED_NAME
        defaultCarServiceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServicesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where name is not null
        defaultCarServiceShouldBeFound("name.specified=true");

        // Get all the carServiceList where name is null
        defaultCarServiceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCarServicesByNameContainsSomething() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where name contains DEFAULT_NAME
        defaultCarServiceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the carServiceList where name contains UPDATED_NAME
        defaultCarServiceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServicesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where name does not contain DEFAULT_NAME
        defaultCarServiceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the carServiceList where name does not contain UPDATED_NAME
        defaultCarServiceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServicesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where address equals to DEFAULT_ADDRESS
        defaultCarServiceShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the carServiceList where address equals to UPDATED_ADDRESS
        defaultCarServiceShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCarServicesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCarServiceShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the carServiceList where address equals to UPDATED_ADDRESS
        defaultCarServiceShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCarServicesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where address is not null
        defaultCarServiceShouldBeFound("address.specified=true");

        // Get all the carServiceList where address is null
        defaultCarServiceShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCarServicesByAddressContainsSomething() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where address contains DEFAULT_ADDRESS
        defaultCarServiceShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the carServiceList where address contains UPDATED_ADDRESS
        defaultCarServiceShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCarServicesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        // Get all the carServiceList where address does not contain DEFAULT_ADDRESS
        defaultCarServiceShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the carServiceList where address does not contain UPDATED_ADDRESS
        defaultCarServiceShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCarServicesByRepairAppointmentsIsEqualToSomething() throws Exception {
        CarRepairAppointment repairAppointments;
        if (TestUtil.findAll(em, CarRepairAppointment.class).isEmpty()) {
            carServiceRepository.saveAndFlush(carService);
            repairAppointments = CarRepairAppointmentResourceIT.createEntity(em);
        } else {
            repairAppointments = TestUtil.findAll(em, CarRepairAppointment.class).get(0);
        }
        em.persist(repairAppointments);
        em.flush();
        carService.addRepairAppointments(repairAppointments);
        carServiceRepository.saveAndFlush(carService);
        Long repairAppointmentsId = repairAppointments.getId();
        // Get all the carServiceList where repairAppointments equals to repairAppointmentsId
        defaultCarServiceShouldBeFound("repairAppointmentsId.equals=" + repairAppointmentsId);

        // Get all the carServiceList where repairAppointments equals to (repairAppointmentsId + 1)
        defaultCarServiceShouldNotBeFound("repairAppointmentsId.equals=" + (repairAppointmentsId + 1));
    }

    @Test
    @Transactional
    void getAllCarServicesByEmployeesIsEqualToSomething() throws Exception {
        CarServiceEmployee employees;
        if (TestUtil.findAll(em, CarServiceEmployee.class).isEmpty()) {
            carServiceRepository.saveAndFlush(carService);
            employees = CarServiceEmployeeResourceIT.createEntity(em);
        } else {
            employees = TestUtil.findAll(em, CarServiceEmployee.class).get(0);
        }
        em.persist(employees);
        em.flush();
        carService.addEmployees(employees);
        carServiceRepository.saveAndFlush(carService);
        Long employeesId = employees.getId();
        // Get all the carServiceList where employees equals to employeesId
        defaultCarServiceShouldBeFound("employeesId.equals=" + employeesId);

        // Get all the carServiceList where employees equals to (employeesId + 1)
        defaultCarServiceShouldNotBeFound("employeesId.equals=" + (employeesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarServiceShouldBeFound(String filter) throws Exception {
        restCarServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restCarServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarServiceShouldNotBeFound(String filter) throws Exception {
        restCarServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarService() throws Exception {
        // Get the carService
        restCarServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarService() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();

        // Update the carService
        CarService updatedCarService = carServiceRepository.findById(carService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarService are not directly saved in db
        em.detach(updatedCarService);
        updatedCarService.name(UPDATED_NAME).address(UPDATED_ADDRESS);
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(updatedCarService);

        restCarServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
        CarService testCarService = carServiceList.get(carServiceList.size() - 1);
        assertThat(testCarService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarService.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingCarService() throws Exception {
        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();
        carService.setId(longCount.incrementAndGet());

        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarService() throws Exception {
        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();
        carService.setId(longCount.incrementAndGet());

        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarService() throws Exception {
        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();
        carService.setId(longCount.incrementAndGet());

        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarServiceWithPatch() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();

        // Update the carService using partial update
        CarService partialUpdatedCarService = new CarService();
        partialUpdatedCarService.setId(carService.getId());

        partialUpdatedCarService.address(UPDATED_ADDRESS);

        restCarServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarService))
            )
            .andExpect(status().isOk());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
        CarService testCarService = carServiceList.get(carServiceList.size() - 1);
        assertThat(testCarService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarService.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateCarServiceWithPatch() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();

        // Update the carService using partial update
        CarService partialUpdatedCarService = new CarService();
        partialUpdatedCarService.setId(carService.getId());

        partialUpdatedCarService.name(UPDATED_NAME).address(UPDATED_ADDRESS);

        restCarServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarService))
            )
            .andExpect(status().isOk());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
        CarService testCarService = carServiceList.get(carServiceList.size() - 1);
        assertThat(testCarService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarService.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingCarService() throws Exception {
        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();
        carService.setId(longCount.incrementAndGet());

        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarService() throws Exception {
        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();
        carService.setId(longCount.incrementAndGet());

        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarService() throws Exception {
        int databaseSizeBeforeUpdate = carServiceRepository.findAll().size();
        carService.setId(longCount.incrementAndGet());

        // Create the CarService
        CarServiceDTO carServiceDTO = carServiceMapper.toDto(carService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarService in the database
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarService() throws Exception {
        // Initialize the database
        carServiceRepository.saveAndFlush(carService);

        int databaseSizeBeforeDelete = carServiceRepository.findAll().size();

        // Delete the carService
        restCarServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, carService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarService> carServiceList = carServiceRepository.findAll();
        assertThat(carServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
