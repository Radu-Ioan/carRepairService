package com.carrepairservice.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carrepairservice.app.IntegrationTest;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.domain.CarService;
import com.carrepairservice.app.domain.CarServiceEmployee;
import com.carrepairservice.app.repository.CarServiceEmployeeRepository;
import com.carrepairservice.app.service.CarServiceEmployeeService;
import com.carrepairservice.app.service.dto.CarServiceEmployeeDTO;
import com.carrepairservice.app.service.mapper.CarServiceEmployeeMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarServiceEmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarServiceEmployeeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 2;
    private static final Integer UPDATED_AGE = 3;
    private static final Integer SMALLER_AGE = 2 - 1;

    private static final Integer DEFAULT_YEARS_OF_EXPERIENCE = 0;
    private static final Integer UPDATED_YEARS_OF_EXPERIENCE = 1;
    private static final Integer SMALLER_YEARS_OF_EXPERIENCE = 0 - 1;

    private static final String ENTITY_API_URL = "/api/car-service-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarServiceEmployeeRepository carServiceEmployeeRepository;

    @Mock
    private CarServiceEmployeeRepository carServiceEmployeeRepositoryMock;

    @Autowired
    private CarServiceEmployeeMapper carServiceEmployeeMapper;

    @Mock
    private CarServiceEmployeeService carServiceEmployeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarServiceEmployeeMockMvc;

    private CarServiceEmployee carServiceEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarServiceEmployee createEntity(EntityManager em) {
        CarServiceEmployee carServiceEmployee = new CarServiceEmployee()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .yearsOfExperience(DEFAULT_YEARS_OF_EXPERIENCE);
        // Add required entity
        CarService carService;
        if (TestUtil.findAll(em, CarService.class).isEmpty()) {
            carService = CarServiceResourceIT.createEntity(em);
            em.persist(carService);
            em.flush();
        } else {
            carService = TestUtil.findAll(em, CarService.class).get(0);
        }
        carServiceEmployee.setCarService(carService);
        return carServiceEmployee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarServiceEmployee createUpdatedEntity(EntityManager em) {
        CarServiceEmployee carServiceEmployee = new CarServiceEmployee()
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE);
        // Add required entity
        CarService carService;
        if (TestUtil.findAll(em, CarService.class).isEmpty()) {
            carService = CarServiceResourceIT.createUpdatedEntity(em);
            em.persist(carService);
            em.flush();
        } else {
            carService = TestUtil.findAll(em, CarService.class).get(0);
        }
        carServiceEmployee.setCarService(carService);
        return carServiceEmployee;
    }

    @BeforeEach
    public void initTest() {
        carServiceEmployee = createEntity(em);
    }

    @Test
    @Transactional
    void createCarServiceEmployee() throws Exception {
        int databaseSizeBeforeCreate = carServiceEmployeeRepository.findAll().size();
        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);
        restCarServiceEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        CarServiceEmployee testCarServiceEmployee = carServiceEmployeeList.get(carServiceEmployeeList.size() - 1);
        assertThat(testCarServiceEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarServiceEmployee.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCarServiceEmployee.getYearsOfExperience()).isEqualTo(DEFAULT_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void createCarServiceEmployeeWithExistingId() throws Exception {
        // Create the CarServiceEmployee with an existing ID
        carServiceEmployee.setId(1L);
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        int databaseSizeBeforeCreate = carServiceEmployeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarServiceEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carServiceEmployeeRepository.findAll().size();
        // set the field null
        carServiceEmployee.setName(null);

        // Create the CarServiceEmployee, which fails.
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        restCarServiceEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployees() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList
        restCarServiceEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carServiceEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarServiceEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(carServiceEmployeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarServiceEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carServiceEmployeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarServiceEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carServiceEmployeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarServiceEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carServiceEmployeeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCarServiceEmployee() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get the carServiceEmployee
        restCarServiceEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, carServiceEmployee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carServiceEmployee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.yearsOfExperience").value(DEFAULT_YEARS_OF_EXPERIENCE));
    }

    @Test
    @Transactional
    void getCarServiceEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        Long id = carServiceEmployee.getId();

        defaultCarServiceEmployeeShouldBeFound("id.equals=" + id);
        defaultCarServiceEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultCarServiceEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarServiceEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultCarServiceEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarServiceEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where name equals to DEFAULT_NAME
        defaultCarServiceEmployeeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the carServiceEmployeeList where name equals to UPDATED_NAME
        defaultCarServiceEmployeeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCarServiceEmployeeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the carServiceEmployeeList where name equals to UPDATED_NAME
        defaultCarServiceEmployeeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where name is not null
        defaultCarServiceEmployeeShouldBeFound("name.specified=true");

        // Get all the carServiceEmployeeList where name is null
        defaultCarServiceEmployeeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByNameContainsSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where name contains DEFAULT_NAME
        defaultCarServiceEmployeeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the carServiceEmployeeList where name contains UPDATED_NAME
        defaultCarServiceEmployeeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where name does not contain DEFAULT_NAME
        defaultCarServiceEmployeeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the carServiceEmployeeList where name does not contain UPDATED_NAME
        defaultCarServiceEmployeeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age equals to DEFAULT_AGE
        defaultCarServiceEmployeeShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the carServiceEmployeeList where age equals to UPDATED_AGE
        defaultCarServiceEmployeeShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age in DEFAULT_AGE or UPDATED_AGE
        defaultCarServiceEmployeeShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the carServiceEmployeeList where age equals to UPDATED_AGE
        defaultCarServiceEmployeeShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age is not null
        defaultCarServiceEmployeeShouldBeFound("age.specified=true");

        // Get all the carServiceEmployeeList where age is null
        defaultCarServiceEmployeeShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age is greater than or equal to DEFAULT_AGE
        defaultCarServiceEmployeeShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the carServiceEmployeeList where age is greater than or equal to (DEFAULT_AGE + 1)
        defaultCarServiceEmployeeShouldNotBeFound("age.greaterThanOrEqual=" + (DEFAULT_AGE + 1));
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age is less than or equal to DEFAULT_AGE
        defaultCarServiceEmployeeShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the carServiceEmployeeList where age is less than or equal to SMALLER_AGE
        defaultCarServiceEmployeeShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age is less than DEFAULT_AGE
        defaultCarServiceEmployeeShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the carServiceEmployeeList where age is less than (DEFAULT_AGE + 1)
        defaultCarServiceEmployeeShouldBeFound("age.lessThan=" + (DEFAULT_AGE + 1));
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where age is greater than DEFAULT_AGE
        defaultCarServiceEmployeeShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the carServiceEmployeeList where age is greater than SMALLER_AGE
        defaultCarServiceEmployeeShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience equals to DEFAULT_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.equals=" + DEFAULT_YEARS_OF_EXPERIENCE);

        // Get all the carServiceEmployeeList where yearsOfExperience equals to UPDATED_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.equals=" + UPDATED_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience in DEFAULT_YEARS_OF_EXPERIENCE or UPDATED_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.in=" + DEFAULT_YEARS_OF_EXPERIENCE + "," + UPDATED_YEARS_OF_EXPERIENCE);

        // Get all the carServiceEmployeeList where yearsOfExperience equals to UPDATED_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.in=" + UPDATED_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience is not null
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.specified=true");

        // Get all the carServiceEmployeeList where yearsOfExperience is null
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.specified=false");
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience is greater than or equal to DEFAULT_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.greaterThanOrEqual=" + DEFAULT_YEARS_OF_EXPERIENCE);

        // Get all the carServiceEmployeeList where yearsOfExperience is greater than or equal to UPDATED_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.greaterThanOrEqual=" + UPDATED_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience is less than or equal to DEFAULT_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.lessThanOrEqual=" + DEFAULT_YEARS_OF_EXPERIENCE);

        // Get all the carServiceEmployeeList where yearsOfExperience is less than or equal to SMALLER_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.lessThanOrEqual=" + SMALLER_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsLessThanSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience is less than DEFAULT_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.lessThan=" + DEFAULT_YEARS_OF_EXPERIENCE);

        // Get all the carServiceEmployeeList where yearsOfExperience is less than UPDATED_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.lessThan=" + UPDATED_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByYearsOfExperienceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        // Get all the carServiceEmployeeList where yearsOfExperience is greater than DEFAULT_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldNotBeFound("yearsOfExperience.greaterThan=" + DEFAULT_YEARS_OF_EXPERIENCE);

        // Get all the carServiceEmployeeList where yearsOfExperience is greater than SMALLER_YEARS_OF_EXPERIENCE
        defaultCarServiceEmployeeShouldBeFound("yearsOfExperience.greaterThan=" + SMALLER_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByCarServiceIsEqualToSomething() throws Exception {
        CarService carService;
        if (TestUtil.findAll(em, CarService.class).isEmpty()) {
            carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);
            carService = CarServiceResourceIT.createEntity(em);
        } else {
            carService = TestUtil.findAll(em, CarService.class).get(0);
        }
        em.persist(carService);
        em.flush();
        carServiceEmployee.setCarService(carService);
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);
        Long carServiceId = carService.getId();
        // Get all the carServiceEmployeeList where carService equals to carServiceId
        defaultCarServiceEmployeeShouldBeFound("carServiceId.equals=" + carServiceId);

        // Get all the carServiceEmployeeList where carService equals to (carServiceId + 1)
        defaultCarServiceEmployeeShouldNotBeFound("carServiceId.equals=" + (carServiceId + 1));
    }

    @Test
    @Transactional
    void getAllCarServiceEmployeesByRepairAppointmentsIsEqualToSomething() throws Exception {
        CarRepairAppointment repairAppointments;
        if (TestUtil.findAll(em, CarRepairAppointment.class).isEmpty()) {
            carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);
            repairAppointments = CarRepairAppointmentResourceIT.createEntity(em);
        } else {
            repairAppointments = TestUtil.findAll(em, CarRepairAppointment.class).get(0);
        }
        em.persist(repairAppointments);
        em.flush();
        carServiceEmployee.addRepairAppointments(repairAppointments);
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);
        Long repairAppointmentsId = repairAppointments.getId();
        // Get all the carServiceEmployeeList where repairAppointments equals to repairAppointmentsId
        defaultCarServiceEmployeeShouldBeFound("repairAppointmentsId.equals=" + repairAppointmentsId);

        // Get all the carServiceEmployeeList where repairAppointments equals to (repairAppointmentsId + 1)
        defaultCarServiceEmployeeShouldNotBeFound("repairAppointmentsId.equals=" + (repairAppointmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarServiceEmployeeShouldBeFound(String filter) throws Exception {
        restCarServiceEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carServiceEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)));

        // Check, that the count call also returns 1
        restCarServiceEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarServiceEmployeeShouldNotBeFound(String filter) throws Exception {
        restCarServiceEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarServiceEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarServiceEmployee() throws Exception {
        // Get the carServiceEmployee
        restCarServiceEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarServiceEmployee() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();

        // Update the carServiceEmployee
        CarServiceEmployee updatedCarServiceEmployee = carServiceEmployeeRepository.findById(carServiceEmployee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarServiceEmployee are not directly saved in db
        em.detach(updatedCarServiceEmployee);
        updatedCarServiceEmployee.name(UPDATED_NAME).age(UPDATED_AGE).yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE);
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(updatedCarServiceEmployee);

        restCarServiceEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carServiceEmployeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CarServiceEmployee testCarServiceEmployee = carServiceEmployeeList.get(carServiceEmployeeList.size() - 1);
        assertThat(testCarServiceEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarServiceEmployee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCarServiceEmployee.getYearsOfExperience()).isEqualTo(UPDATED_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void putNonExistingCarServiceEmployee() throws Exception {
        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();
        carServiceEmployee.setId(longCount.incrementAndGet());

        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarServiceEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carServiceEmployeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarServiceEmployee() throws Exception {
        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();
        carServiceEmployee.setId(longCount.incrementAndGet());

        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarServiceEmployee() throws Exception {
        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();
        carServiceEmployee.setId(longCount.incrementAndGet());

        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarServiceEmployeeWithPatch() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();

        // Update the carServiceEmployee using partial update
        CarServiceEmployee partialUpdatedCarServiceEmployee = new CarServiceEmployee();
        partialUpdatedCarServiceEmployee.setId(carServiceEmployee.getId());

        partialUpdatedCarServiceEmployee.name(UPDATED_NAME).age(UPDATED_AGE);

        restCarServiceEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarServiceEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarServiceEmployee))
            )
            .andExpect(status().isOk());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CarServiceEmployee testCarServiceEmployee = carServiceEmployeeList.get(carServiceEmployeeList.size() - 1);
        assertThat(testCarServiceEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarServiceEmployee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCarServiceEmployee.getYearsOfExperience()).isEqualTo(DEFAULT_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void fullUpdateCarServiceEmployeeWithPatch() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();

        // Update the carServiceEmployee using partial update
        CarServiceEmployee partialUpdatedCarServiceEmployee = new CarServiceEmployee();
        partialUpdatedCarServiceEmployee.setId(carServiceEmployee.getId());

        partialUpdatedCarServiceEmployee.name(UPDATED_NAME).age(UPDATED_AGE).yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE);

        restCarServiceEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarServiceEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarServiceEmployee))
            )
            .andExpect(status().isOk());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CarServiceEmployee testCarServiceEmployee = carServiceEmployeeList.get(carServiceEmployeeList.size() - 1);
        assertThat(testCarServiceEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarServiceEmployee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCarServiceEmployee.getYearsOfExperience()).isEqualTo(UPDATED_YEARS_OF_EXPERIENCE);
    }

    @Test
    @Transactional
    void patchNonExistingCarServiceEmployee() throws Exception {
        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();
        carServiceEmployee.setId(longCount.incrementAndGet());

        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarServiceEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carServiceEmployeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarServiceEmployee() throws Exception {
        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();
        carServiceEmployee.setId(longCount.incrementAndGet());

        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarServiceEmployee() throws Exception {
        int databaseSizeBeforeUpdate = carServiceEmployeeRepository.findAll().size();
        carServiceEmployee.setId(longCount.incrementAndGet());

        // Create the CarServiceEmployee
        CarServiceEmployeeDTO carServiceEmployeeDTO = carServiceEmployeeMapper.toDto(carServiceEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarServiceEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carServiceEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarServiceEmployee in the database
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarServiceEmployee() throws Exception {
        // Initialize the database
        carServiceEmployeeRepository.saveAndFlush(carServiceEmployee);

        int databaseSizeBeforeDelete = carServiceEmployeeRepository.findAll().size();

        // Delete the carServiceEmployee
        restCarServiceEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, carServiceEmployee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarServiceEmployee> carServiceEmployeeList = carServiceEmployeeRepository.findAll();
        assertThat(carServiceEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
