package com.carrepairservice.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carrepairservice.app.IntegrationTest;
import com.carrepairservice.app.domain.CarRepairAppointment;
import com.carrepairservice.app.repository.CarRepairAppointmentRepository;
import com.carrepairservice.app.service.dto.CarRepairAppointmentDTO;
import com.carrepairservice.app.service.mapper.CarRepairAppointmentMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CarRepairAppointmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarRepairAppointmentResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/car-repair-appointments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepairAppointmentRepository carRepairAppointmentRepository;

    @Autowired
    private CarRepairAppointmentMapper carRepairAppointmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarRepairAppointmentMockMvc;

    private CarRepairAppointment carRepairAppointment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarRepairAppointment createEntity(EntityManager em) {
        CarRepairAppointment carRepairAppointment = new CarRepairAppointment().date(DEFAULT_DATE);
        return carRepairAppointment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarRepairAppointment createUpdatedEntity(EntityManager em) {
        CarRepairAppointment carRepairAppointment = new CarRepairAppointment().date(UPDATED_DATE);
        return carRepairAppointment;
    }

    @BeforeEach
    public void initTest() {
        carRepairAppointment = createEntity(em);
    }

    @Test
    @Transactional
    void createCarRepairAppointment() throws Exception {
        int databaseSizeBeforeCreate = carRepairAppointmentRepository.findAll().size();
        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);
        restCarRepairAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeCreate + 1);
        CarRepairAppointment testCarRepairAppointment = carRepairAppointmentList.get(carRepairAppointmentList.size() - 1);
        assertThat(testCarRepairAppointment.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createCarRepairAppointmentWithExistingId() throws Exception {
        // Create the CarRepairAppointment with an existing ID
        carRepairAppointment.setId(1L);
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        int databaseSizeBeforeCreate = carRepairAppointmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarRepairAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepairAppointmentRepository.findAll().size();
        // set the field null
        carRepairAppointment.setDate(null);

        // Create the CarRepairAppointment, which fails.
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        restCarRepairAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointments() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList
        restCarRepairAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carRepairAppointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getCarRepairAppointment() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get the carRepairAppointment
        restCarRepairAppointmentMockMvc
            .perform(get(ENTITY_API_URL_ID, carRepairAppointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carRepairAppointment.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getCarRepairAppointmentsByIdFiltering() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        Long id = carRepairAppointment.getId();

        defaultCarRepairAppointmentShouldBeFound("id.equals=" + id);
        defaultCarRepairAppointmentShouldNotBeFound("id.notEquals=" + id);

        defaultCarRepairAppointmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarRepairAppointmentShouldNotBeFound("id.greaterThan=" + id);

        defaultCarRepairAppointmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarRepairAppointmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date equals to DEFAULT_DATE
        defaultCarRepairAppointmentShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the carRepairAppointmentList where date equals to UPDATED_DATE
        defaultCarRepairAppointmentShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCarRepairAppointmentShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the carRepairAppointmentList where date equals to UPDATED_DATE
        defaultCarRepairAppointmentShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date is not null
        defaultCarRepairAppointmentShouldBeFound("date.specified=true");

        // Get all the carRepairAppointmentList where date is null
        defaultCarRepairAppointmentShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date is greater than or equal to DEFAULT_DATE
        defaultCarRepairAppointmentShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the carRepairAppointmentList where date is greater than or equal to UPDATED_DATE
        defaultCarRepairAppointmentShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date is less than or equal to DEFAULT_DATE
        defaultCarRepairAppointmentShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the carRepairAppointmentList where date is less than or equal to SMALLER_DATE
        defaultCarRepairAppointmentShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date is less than DEFAULT_DATE
        defaultCarRepairAppointmentShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the carRepairAppointmentList where date is less than UPDATED_DATE
        defaultCarRepairAppointmentShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCarRepairAppointmentsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        // Get all the carRepairAppointmentList where date is greater than DEFAULT_DATE
        defaultCarRepairAppointmentShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the carRepairAppointmentList where date is greater than SMALLER_DATE
        defaultCarRepairAppointmentShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarRepairAppointmentShouldBeFound(String filter) throws Exception {
        restCarRepairAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carRepairAppointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restCarRepairAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarRepairAppointmentShouldNotBeFound(String filter) throws Exception {
        restCarRepairAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarRepairAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarRepairAppointment() throws Exception {
        // Get the carRepairAppointment
        restCarRepairAppointmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarRepairAppointment() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();

        // Update the carRepairAppointment
        CarRepairAppointment updatedCarRepairAppointment = carRepairAppointmentRepository
            .findById(carRepairAppointment.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCarRepairAppointment are not directly saved in db
        em.detach(updatedCarRepairAppointment);
        updatedCarRepairAppointment.date(UPDATED_DATE);
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(updatedCarRepairAppointment);

        restCarRepairAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carRepairAppointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
        CarRepairAppointment testCarRepairAppointment = carRepairAppointmentList.get(carRepairAppointmentList.size() - 1);
        assertThat(testCarRepairAppointment.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCarRepairAppointment() throws Exception {
        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();
        carRepairAppointment.setId(longCount.incrementAndGet());

        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarRepairAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carRepairAppointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarRepairAppointment() throws Exception {
        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();
        carRepairAppointment.setId(longCount.incrementAndGet());

        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRepairAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarRepairAppointment() throws Exception {
        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();
        carRepairAppointment.setId(longCount.incrementAndGet());

        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRepairAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarRepairAppointmentWithPatch() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();

        // Update the carRepairAppointment using partial update
        CarRepairAppointment partialUpdatedCarRepairAppointment = new CarRepairAppointment();
        partialUpdatedCarRepairAppointment.setId(carRepairAppointment.getId());

        restCarRepairAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarRepairAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarRepairAppointment))
            )
            .andExpect(status().isOk());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
        CarRepairAppointment testCarRepairAppointment = carRepairAppointmentList.get(carRepairAppointmentList.size() - 1);
        assertThat(testCarRepairAppointment.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCarRepairAppointmentWithPatch() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();

        // Update the carRepairAppointment using partial update
        CarRepairAppointment partialUpdatedCarRepairAppointment = new CarRepairAppointment();
        partialUpdatedCarRepairAppointment.setId(carRepairAppointment.getId());

        partialUpdatedCarRepairAppointment.date(UPDATED_DATE);

        restCarRepairAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarRepairAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarRepairAppointment))
            )
            .andExpect(status().isOk());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
        CarRepairAppointment testCarRepairAppointment = carRepairAppointmentList.get(carRepairAppointmentList.size() - 1);
        assertThat(testCarRepairAppointment.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCarRepairAppointment() throws Exception {
        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();
        carRepairAppointment.setId(longCount.incrementAndGet());

        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarRepairAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carRepairAppointmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarRepairAppointment() throws Exception {
        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();
        carRepairAppointment.setId(longCount.incrementAndGet());

        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRepairAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarRepairAppointment() throws Exception {
        int databaseSizeBeforeUpdate = carRepairAppointmentRepository.findAll().size();
        carRepairAppointment.setId(longCount.incrementAndGet());

        // Create the CarRepairAppointment
        CarRepairAppointmentDTO carRepairAppointmentDTO = carRepairAppointmentMapper.toDto(carRepairAppointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRepairAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carRepairAppointmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarRepairAppointment in the database
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarRepairAppointment() throws Exception {
        // Initialize the database
        carRepairAppointmentRepository.saveAndFlush(carRepairAppointment);

        int databaseSizeBeforeDelete = carRepairAppointmentRepository.findAll().size();

        // Delete the carRepairAppointment
        restCarRepairAppointmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, carRepairAppointment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarRepairAppointment> carRepairAppointmentList = carRepairAppointmentRepository.findAll();
        assertThat(carRepairAppointmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
