package com.microservices.serviceone.web.rest;

import static com.microservices.serviceone.domain.BrigadeAsserts.*;
import static com.microservices.serviceone.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.serviceone.IntegrationTest;
import com.microservices.serviceone.domain.Brigade;
import com.microservices.serviceone.domain.Governorate;
import com.microservices.serviceone.repository.BrigadeRepository;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.service.mapper.BrigadeMapper;
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
 * Integration tests for the {@link BrigadeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BrigadeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTABLISHED_YEAR = 1;
    private static final Integer UPDATED_ESTABLISHED_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/brigades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BrigadeRepository brigadeRepository;

    @Autowired
    private BrigadeMapper brigadeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBrigadeMockMvc;

    private Brigade brigade;

    private Brigade insertedBrigade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brigade createEntity(EntityManager em) {
        Brigade brigade = new Brigade().name(DEFAULT_NAME).type(DEFAULT_TYPE).establishedYear(DEFAULT_ESTABLISHED_YEAR);
        // Add required entity
        Governorate governorate;
        if (TestUtil.findAll(em, Governorate.class).isEmpty()) {
            governorate = GovernorateResourceIT.createEntity();
            em.persist(governorate);
            em.flush();
        } else {
            governorate = TestUtil.findAll(em, Governorate.class).get(0);
        }
        brigade.setGovernorate(governorate);
        return brigade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brigade createUpdatedEntity(EntityManager em) {
        Brigade updatedBrigade = new Brigade().name(UPDATED_NAME).type(UPDATED_TYPE).establishedYear(UPDATED_ESTABLISHED_YEAR);
        // Add required entity
        Governorate governorate;
        if (TestUtil.findAll(em, Governorate.class).isEmpty()) {
            governorate = GovernorateResourceIT.createUpdatedEntity();
            em.persist(governorate);
            em.flush();
        } else {
            governorate = TestUtil.findAll(em, Governorate.class).get(0);
        }
        updatedBrigade.setGovernorate(governorate);
        return updatedBrigade;
    }

    @BeforeEach
    public void initTest() {
        brigade = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBrigade != null) {
            brigadeRepository.delete(insertedBrigade);
            insertedBrigade = null;
        }
    }

    @Test
    @Transactional
    void createBrigade() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);
        var returnedBrigadeDTO = om.readValue(
            restBrigadeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brigadeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BrigadeDTO.class
        );

        // Validate the Brigade in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBrigade = brigadeMapper.toEntity(returnedBrigadeDTO);
        assertBrigadeUpdatableFieldsEquals(returnedBrigade, getPersistedBrigade(returnedBrigade));

        insertedBrigade = returnedBrigade;
    }

    @Test
    @Transactional
    void createBrigadeWithExistingId() throws Exception {
        // Create the Brigade with an existing ID
        brigade.setId(1L);
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrigadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brigadeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        brigade.setName(null);

        // Create the Brigade, which fails.
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        restBrigadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brigadeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBrigades() throws Exception {
        // Initialize the database
        insertedBrigade = brigadeRepository.saveAndFlush(brigade);

        // Get all the brigadeList
        restBrigadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brigade.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].establishedYear").value(hasItem(DEFAULT_ESTABLISHED_YEAR)));
    }

    @Test
    @Transactional
    void getBrigade() throws Exception {
        // Initialize the database
        insertedBrigade = brigadeRepository.saveAndFlush(brigade);

        // Get the brigade
        restBrigadeMockMvc
            .perform(get(ENTITY_API_URL_ID, brigade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(brigade.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.establishedYear").value(DEFAULT_ESTABLISHED_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingBrigade() throws Exception {
        // Get the brigade
        restBrigadeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBrigade() throws Exception {
        // Initialize the database
        insertedBrigade = brigadeRepository.saveAndFlush(brigade);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brigade
        Brigade updatedBrigade = brigadeRepository.findById(brigade.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBrigade are not directly saved in db
        em.detach(updatedBrigade);
        updatedBrigade.name(UPDATED_NAME).type(UPDATED_TYPE).establishedYear(UPDATED_ESTABLISHED_YEAR);
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(updatedBrigade);

        restBrigadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brigadeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brigadeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBrigadeToMatchAllProperties(updatedBrigade);
    }

    @Test
    @Transactional
    void putNonExistingBrigade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brigade.setId(longCount.incrementAndGet());

        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrigadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brigadeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brigadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBrigade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brigade.setId(longCount.incrementAndGet());

        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrigadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(brigadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBrigade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brigade.setId(longCount.incrementAndGet());

        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrigadeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brigadeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBrigadeWithPatch() throws Exception {
        // Initialize the database
        insertedBrigade = brigadeRepository.saveAndFlush(brigade);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brigade using partial update
        Brigade partialUpdatedBrigade = new Brigade();
        partialUpdatedBrigade.setId(brigade.getId());

        restBrigadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrigade.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBrigade))
            )
            .andExpect(status().isOk());

        // Validate the Brigade in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBrigadeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBrigade, brigade), getPersistedBrigade(brigade));
    }

    @Test
    @Transactional
    void fullUpdateBrigadeWithPatch() throws Exception {
        // Initialize the database
        insertedBrigade = brigadeRepository.saveAndFlush(brigade);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brigade using partial update
        Brigade partialUpdatedBrigade = new Brigade();
        partialUpdatedBrigade.setId(brigade.getId());

        partialUpdatedBrigade.name(UPDATED_NAME).type(UPDATED_TYPE).establishedYear(UPDATED_ESTABLISHED_YEAR);

        restBrigadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrigade.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBrigade))
            )
            .andExpect(status().isOk());

        // Validate the Brigade in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBrigadeUpdatableFieldsEquals(partialUpdatedBrigade, getPersistedBrigade(partialUpdatedBrigade));
    }

    @Test
    @Transactional
    void patchNonExistingBrigade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brigade.setId(longCount.incrementAndGet());

        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrigadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, brigadeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(brigadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBrigade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brigade.setId(longCount.incrementAndGet());

        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrigadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(brigadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBrigade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brigade.setId(longCount.incrementAndGet());

        // Create the Brigade
        BrigadeDTO brigadeDTO = brigadeMapper.toDto(brigade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrigadeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(brigadeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brigade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBrigade() throws Exception {
        // Initialize the database
        insertedBrigade = brigadeRepository.saveAndFlush(brigade);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the brigade
        restBrigadeMockMvc
            .perform(delete(ENTITY_API_URL_ID, brigade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return brigadeRepository.count();
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

    protected Brigade getPersistedBrigade(Brigade brigade) {
        return brigadeRepository.findById(brigade.getId()).orElseThrow();
    }

    protected void assertPersistedBrigadeToMatchAllProperties(Brigade expectedBrigade) {
        assertBrigadeAllPropertiesEquals(expectedBrigade, getPersistedBrigade(expectedBrigade));
    }

    protected void assertPersistedBrigadeToMatchUpdatableProperties(Brigade expectedBrigade) {
        assertBrigadeAllUpdatablePropertiesEquals(expectedBrigade, getPersistedBrigade(expectedBrigade));
    }
}
