package com.microservices.serviceone.web.rest;

import static com.microservices.serviceone.domain.GovernorateAsserts.*;
import static com.microservices.serviceone.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.serviceone.IntegrationTest;
import com.microservices.serviceone.domain.Governorate;
import com.microservices.serviceone.repository.GovernorateRepository;
import com.microservices.serviceone.service.dto.GovernorateDTO;
import com.microservices.serviceone.service.mapper.GovernorateMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GovernorateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GovernorateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_AREA = 1D;
    private static final Double UPDATED_AREA = 2D;

    private static final Integer DEFAULT_POPULATION = 1;
    private static final Integer UPDATED_POPULATION = 2;

    private static final String ENTITY_API_URL = "/api/governorates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GovernorateRepository governorateRepository;

    @Autowired
    private GovernorateMapper governorateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGovernorateMockMvc;

    private Governorate governorate;

    private Governorate insertedGovernorate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Governorate createEntity() {
        return new Governorate().name(DEFAULT_NAME).area(DEFAULT_AREA).population(DEFAULT_POPULATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Governorate createUpdatedEntity() {
        return new Governorate().name(UPDATED_NAME).area(UPDATED_AREA).population(UPDATED_POPULATION);
    }

    @BeforeEach
    public void initTest() {
        governorate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGovernorate != null) {
            governorateRepository.delete(insertedGovernorate);
            insertedGovernorate = null;
        }
    }

    @Test
    @Transactional
    void createGovernorate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);
        var returnedGovernorateDTO = om.readValue(
            restGovernorateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GovernorateDTO.class
        );

        // Validate the Governorate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGovernorate = governorateMapper.toEntity(returnedGovernorateDTO);
        assertGovernorateUpdatableFieldsEquals(returnedGovernorate, getPersistedGovernorate(returnedGovernorate));

        insertedGovernorate = returnedGovernorate;
    }

    @Test
    @Transactional
    void createGovernorateWithExistingId() throws Exception {
        // Create the Governorate with an existing ID
        governorate.setId(1L);
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGovernorateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        governorate.setName(null);

        // Create the Governorate, which fails.
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        restGovernorateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGovernorates() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(governorate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.doubleValue())))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)));
    }

    @Test
    @Transactional
    void getGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get the governorate
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL_ID, governorate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(governorate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.doubleValue()))
            .andExpect(jsonPath("$.population").value(DEFAULT_POPULATION));
    }

    @Test
    @Transactional
    void getNonExistingGovernorate() throws Exception {
        // Get the governorate
        restGovernorateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the governorate
        Governorate updatedGovernorate = governorateRepository.findById(governorate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGovernorate are not directly saved in db
        em.detach(updatedGovernorate);
        updatedGovernorate.name(UPDATED_NAME).area(UPDATED_AREA).population(UPDATED_POPULATION);
        GovernorateDTO governorateDTO = governorateMapper.toDto(updatedGovernorate);

        restGovernorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, governorateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGovernorateToMatchAllProperties(updatedGovernorate);
    }

    @Test
    @Transactional
    void putNonExistingGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorate.setId(longCount.incrementAndGet());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, governorateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorate.setId(longCount.incrementAndGet());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorate.setId(longCount.incrementAndGet());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGovernorateWithPatch() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the governorate using partial update
        Governorate partialUpdatedGovernorate = new Governorate();
        partialUpdatedGovernorate.setId(governorate.getId());

        partialUpdatedGovernorate.name(UPDATED_NAME).population(UPDATED_POPULATION);

        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGovernorate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGovernorate))
            )
            .andExpect(status().isOk());

        // Validate the Governorate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGovernorateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGovernorate, governorate),
            getPersistedGovernorate(governorate)
        );
    }

    @Test
    @Transactional
    void fullUpdateGovernorateWithPatch() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the governorate using partial update
        Governorate partialUpdatedGovernorate = new Governorate();
        partialUpdatedGovernorate.setId(governorate.getId());

        partialUpdatedGovernorate.name(UPDATED_NAME).area(UPDATED_AREA).population(UPDATED_POPULATION);

        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGovernorate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGovernorate))
            )
            .andExpect(status().isOk());

        // Validate the Governorate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGovernorateUpdatableFieldsEquals(partialUpdatedGovernorate, getPersistedGovernorate(partialUpdatedGovernorate));
    }

    @Test
    @Transactional
    void patchNonExistingGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorate.setId(longCount.incrementAndGet());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, governorateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorate.setId(longCount.incrementAndGet());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorate.setId(longCount.incrementAndGet());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(governorateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the governorate
        restGovernorateMockMvc
            .perform(delete(ENTITY_API_URL_ID, governorate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return governorateRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Governorate getPersistedGovernorate(Governorate governorate) {
        return governorateRepository.findById(governorate.getId()).orElseThrow();
    }

    protected void assertPersistedGovernorateToMatchAllProperties(Governorate expectedGovernorate) {
        assertGovernorateAllPropertiesEquals(expectedGovernorate, getPersistedGovernorate(expectedGovernorate));
    }

    protected void assertPersistedGovernorateToMatchUpdatableProperties(Governorate expectedGovernorate) {
        assertGovernorateAllUpdatablePropertiesEquals(expectedGovernorate, getPersistedGovernorate(expectedGovernorate));
    }
}
