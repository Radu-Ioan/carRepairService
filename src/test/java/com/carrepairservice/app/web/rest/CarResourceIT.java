package com.carrepairservice.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carrepairservice.app.IntegrationTest;
import com.carrepairservice.app.domain.Car;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.repository.CarRepository;
import com.carrepairservice.app.service.CarService;
import com.carrepairservice.app.service.dto.CarDTO;
import com.carrepairservice.app.service.mapper.CarMapper;
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
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final Integer DEFAULT_MANUFACTURED_YEAR = 1970;
    private static final Integer UPDATED_MANUFACTURED_YEAR = 1971;
    private static final Integer SMALLER_MANUFACTURED_YEAR = 1970 - 1;

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepository carRepository;

    @Mock
    private CarRepository carRepositoryMock;

    @Autowired
    private CarMapper carMapper;

    @Mock
    private CarService carServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car().company(DEFAULT_COMPANY).manufacturedYear(DEFAULT_MANUFACTURED_YEAR).ownerName(DEFAULT_OWNER_NAME);
        return car;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car().company(UPDATED_COMPANY).manufacturedYear(UPDATED_MANUFACTURED_YEAR).ownerName(UPDATED_OWNER_NAME);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCar.getManufacturedYear()).isEqualTo(DEFAULT_MANUFACTURED_YEAR);
        assertThat(testCar.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOwnerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setOwnerName(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].manufacturedYear").value(hasItem(DEFAULT_MANUFACTURED_YEAR)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.manufacturedYear").value(DEFAULT_MANUFACTURED_YEAR))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME));
    }

    @Test
    @Transactional
    void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarShouldBeFound("id.equals=" + id);
        defaultCarShouldNotBeFound("id.notEquals=" + id);

        defaultCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.greaterThan=" + id);

        defaultCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where company equals to DEFAULT_COMPANY
        defaultCarShouldBeFound("company.equals=" + DEFAULT_COMPANY);

        // Get all the carList where company equals to UPDATED_COMPANY
        defaultCarShouldNotBeFound("company.equals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCarsByCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where company in DEFAULT_COMPANY or UPDATED_COMPANY
        defaultCarShouldBeFound("company.in=" + DEFAULT_COMPANY + "," + UPDATED_COMPANY);

        // Get all the carList where company equals to UPDATED_COMPANY
        defaultCarShouldNotBeFound("company.in=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCarsByCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where company is not null
        defaultCarShouldBeFound("company.specified=true");

        // Get all the carList where company is null
        defaultCarShouldNotBeFound("company.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByCompanyContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where company contains DEFAULT_COMPANY
        defaultCarShouldBeFound("company.contains=" + DEFAULT_COMPANY);

        // Get all the carList where company contains UPDATED_COMPANY
        defaultCarShouldNotBeFound("company.contains=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCarsByCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where company does not contain DEFAULT_COMPANY
        defaultCarShouldNotBeFound("company.doesNotContain=" + DEFAULT_COMPANY);

        // Get all the carList where company does not contain UPDATED_COMPANY
        defaultCarShouldBeFound("company.doesNotContain=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear equals to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.equals=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear equals to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.equals=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear in DEFAULT_MANUFACTURED_YEAR or UPDATED_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.in=" + DEFAULT_MANUFACTURED_YEAR + "," + UPDATED_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear equals to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.in=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is not null
        defaultCarShouldBeFound("manufacturedYear.specified=true");

        // Get all the carList where manufacturedYear is null
        defaultCarShouldNotBeFound("manufacturedYear.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is greater than or equal to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.greaterThanOrEqual=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is greater than or equal to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.greaterThanOrEqual=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is less than or equal to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.lessThanOrEqual=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is less than or equal to SMALLER_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.lessThanOrEqual=" + SMALLER_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is less than DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.lessThan=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is less than UPDATED_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.lessThan=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturedYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is greater than DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.greaterThan=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is greater than SMALLER_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.greaterThan=" + SMALLER_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByOwnerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where ownerName equals to DEFAULT_OWNER_NAME
        defaultCarShouldBeFound("ownerName.equals=" + DEFAULT_OWNER_NAME);

        // Get all the carList where ownerName equals to UPDATED_OWNER_NAME
        defaultCarShouldNotBeFound("ownerName.equals=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByOwnerNameIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where ownerName in DEFAULT_OWNER_NAME or UPDATED_OWNER_NAME
        defaultCarShouldBeFound("ownerName.in=" + DEFAULT_OWNER_NAME + "," + UPDATED_OWNER_NAME);

        // Get all the carList where ownerName equals to UPDATED_OWNER_NAME
        defaultCarShouldNotBeFound("ownerName.in=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByOwnerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where ownerName is not null
        defaultCarShouldBeFound("ownerName.specified=true");

        // Get all the carList where ownerName is null
        defaultCarShouldNotBeFound("ownerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByOwnerNameContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where ownerName contains DEFAULT_OWNER_NAME
        defaultCarShouldBeFound("ownerName.contains=" + DEFAULT_OWNER_NAME);

        // Get all the carList where ownerName contains UPDATED_OWNER_NAME
        defaultCarShouldNotBeFound("ownerName.contains=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByOwnerNameNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where ownerName does not contain DEFAULT_OWNER_NAME
        defaultCarShouldNotBeFound("ownerName.doesNotContain=" + DEFAULT_OWNER_NAME);

        // Get all the carList where ownerName does not contain UPDATED_OWNER_NAME
        defaultCarShouldBeFound("ownerName.doesNotContain=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByCarRepairAppointmentIsEqualToSomething() throws Exception {
        CarRepairAppointment carRepairAppointment;
        if (TestUtil.findAll(em, CarRepairAppointment.class).isEmpty()) {
            carRepository.saveAndFlush(car);
            carRepairAppointment = CarRepairAppointmentResourceIT.createEntity(em);
        } else {
            carRepairAppointment = TestUtil.findAll(em, CarRepairAppointment.class).get(0);
        }
        em.persist(carRepairAppointment);
        em.flush();
        car.setCarRepairAppointment(carRepairAppointment);
        carRepository.saveAndFlush(car);
        Long carRepairAppointmentId = carRepairAppointment.getId();
        // Get all the carList where carRepairAppointment equals to carRepairAppointmentId
        defaultCarShouldBeFound("carRepairAppointmentId.equals=" + carRepairAppointmentId);

        // Get all the carList where carRepairAppointment equals to (carRepairAppointmentId + 1)
        defaultCarShouldNotBeFound("carRepairAppointmentId.equals=" + (carRepairAppointmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].manufacturedYear").value(hasItem(DEFAULT_MANUFACTURED_YEAR)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)));

        // Check, that the count call also returns 1
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar.company(UPDATED_COMPANY).manufacturedYear(UPDATED_MANUFACTURED_YEAR).ownerName(UPDATED_OWNER_NAME);
        CarDTO carDTO = carMapper.toDto(updatedCar);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCar.getManufacturedYear()).isEqualTo(UPDATED_MANUFACTURED_YEAR);
        assertThat(testCar.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.manufacturedYear(UPDATED_MANUFACTURED_YEAR).ownerName(UPDATED_OWNER_NAME);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCar.getManufacturedYear()).isEqualTo(UPDATED_MANUFACTURED_YEAR);
        assertThat(testCar.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.company(UPDATED_COMPANY).manufacturedYear(UPDATED_MANUFACTURED_YEAR).ownerName(UPDATED_OWNER_NAME);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCar.getManufacturedYear()).isEqualTo(UPDATED_MANUFACTURED_YEAR);
        assertThat(testCar.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
