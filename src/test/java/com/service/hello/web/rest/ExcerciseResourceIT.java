package com.service.hello.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.service.hello.IntegrationTest;
import com.service.hello.domain.Excercise;
import com.service.hello.domain.enumeration.ExcerciseType;
import com.service.hello.repository.ExcerciseRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExcerciseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExcerciseResourceIT {

    private static final ExcerciseType DEFAULT_TYPE = ExcerciseType.BARBELL;
    private static final ExcerciseType UPDATED_TYPE = ExcerciseType.BAR;

    private static final Integer DEFAULT_CURRENT_VOLUME = 1;
    private static final Integer UPDATED_CURRENT_VOLUME = 2;

    private static final Integer DEFAULT_STARTING_VOLUME = 1;
    private static final Integer UPDATED_STARTING_VOLUME = 2;

    private static final Integer DEFAULT_GOAL_VOLUME = 1;
    private static final Integer UPDATED_GOAL_VOLUME = 2;

    private static final String ENTITY_API_URL = "/api/excercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExcerciseRepository excerciseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExcerciseMockMvc;

    private Excercise excercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Excercise createEntity(EntityManager em) {
        Excercise excercise = new Excercise()
            .type(DEFAULT_TYPE)
            .currentVolume(DEFAULT_CURRENT_VOLUME)
            .startingVolume(DEFAULT_STARTING_VOLUME)
            .goalVolume(DEFAULT_GOAL_VOLUME);
        return excercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Excercise createUpdatedEntity(EntityManager em) {
        Excercise excercise = new Excercise()
            .type(UPDATED_TYPE)
            .currentVolume(UPDATED_CURRENT_VOLUME)
            .startingVolume(UPDATED_STARTING_VOLUME)
            .goalVolume(UPDATED_GOAL_VOLUME);
        return excercise;
    }

    @BeforeEach
    public void initTest() {
        excercise = createEntity(em);
    }

    @Test
    @Transactional
    void createExcercise() throws Exception {
        int databaseSizeBeforeCreate = excerciseRepository.findAll().size();
        // Create the Excercise
        restExcerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excercise)))
            .andExpect(status().isCreated());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(DEFAULT_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(DEFAULT_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(DEFAULT_GOAL_VOLUME);
    }

    @Test
    @Transactional
    void createExcerciseWithExistingId() throws Exception {
        // Create the Excercise with an existing ID
        excercise.setId(1L);

        int databaseSizeBeforeCreate = excerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excercise)))
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExcercises() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList
        restExcerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].currentVolume").value(hasItem(DEFAULT_CURRENT_VOLUME)))
            .andExpect(jsonPath("$.[*].startingVolume").value(hasItem(DEFAULT_STARTING_VOLUME)))
            .andExpect(jsonPath("$.[*].goalVolume").value(hasItem(DEFAULT_GOAL_VOLUME)));
    }

    @Test
    @Transactional
    void getExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get the excercise
        restExcerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, excercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(excercise.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.currentVolume").value(DEFAULT_CURRENT_VOLUME))
            .andExpect(jsonPath("$.startingVolume").value(DEFAULT_STARTING_VOLUME))
            .andExpect(jsonPath("$.goalVolume").value(DEFAULT_GOAL_VOLUME));
    }

    @Test
    @Transactional
    void getNonExistingExcercise() throws Exception {
        // Get the excercise
        restExcerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise
        Excercise updatedExcercise = excerciseRepository.findById(excercise.getId()).get();
        // Disconnect from session so that the updates on updatedExcercise are not directly saved in db
        em.detach(updatedExcercise);
        updatedExcercise
            .type(UPDATED_TYPE)
            .currentVolume(UPDATED_CURRENT_VOLUME)
            .startingVolume(UPDATED_STARTING_VOLUME)
            .goalVolume(UPDATED_GOAL_VOLUME);

        restExcerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExcercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExcercise))
            )
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(UPDATED_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(UPDATED_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(UPDATED_GOAL_VOLUME);
    }

    @Test
    @Transactional
    void putNonExistingExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excercise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExcerciseWithPatch() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise using partial update
        Excercise partialUpdatedExcercise = new Excercise();
        partialUpdatedExcercise.setId(excercise.getId());

        partialUpdatedExcercise.type(UPDATED_TYPE).goalVolume(UPDATED_GOAL_VOLUME);

        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcercise))
            )
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(DEFAULT_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(DEFAULT_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(UPDATED_GOAL_VOLUME);
    }

    @Test
    @Transactional
    void fullUpdateExcerciseWithPatch() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise using partial update
        Excercise partialUpdatedExcercise = new Excercise();
        partialUpdatedExcercise.setId(excercise.getId());

        partialUpdatedExcercise
            .type(UPDATED_TYPE)
            .currentVolume(UPDATED_CURRENT_VOLUME)
            .startingVolume(UPDATED_STARTING_VOLUME)
            .goalVolume(UPDATED_GOAL_VOLUME);

        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcercise))
            )
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(UPDATED_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(UPDATED_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(UPDATED_GOAL_VOLUME);
    }

    @Test
    @Transactional
    void patchNonExistingExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, excercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        int databaseSizeBeforeDelete = excerciseRepository.findAll().size();

        // Delete the excercise
        restExcerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, excercise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
