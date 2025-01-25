package org.jhipster.acervolivraria.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster.acervolivraria.domain.PosicaoAsserts.*;
import static org.jhipster.acervolivraria.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.jhipster.acervolivraria.IntegrationTest;
import org.jhipster.acervolivraria.domain.Posicao;
import org.jhipster.acervolivraria.repository.PosicaoRepository;
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
 * Integration tests for the {@link PosicaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PosicaoResourceIT {

    private static final String DEFAULT_CODIGO = "X9-7860-d54";
    private static final String UPDATED_CODIGO = "Q0-960-r688";

    private static final String ENTITY_API_URL = "/api/posicaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PosicaoRepository posicaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPosicaoMockMvc;

    private Posicao posicao;

    private Posicao insertedPosicao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posicao createEntity() {
        return new Posicao().codigo(DEFAULT_CODIGO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posicao createUpdatedEntity() {
        return new Posicao().codigo(UPDATED_CODIGO);
    }

    @BeforeEach
    public void initTest() {
        posicao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPosicao != null) {
            posicaoRepository.delete(insertedPosicao);
            insertedPosicao = null;
        }
    }

    @Test
    @Transactional
    void createPosicao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Posicao
        var returnedPosicao = om.readValue(
            restPosicaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posicao)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Posicao.class
        );

        // Validate the Posicao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPosicaoUpdatableFieldsEquals(returnedPosicao, getPersistedPosicao(returnedPosicao));

        insertedPosicao = returnedPosicao;
    }

    @Test
    @Transactional
    void createPosicaoWithExistingId() throws Exception {
        // Create the Posicao with an existing ID
        posicao.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posicao)))
            .andExpect(status().isBadRequest());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        posicao.setCodigo(null);

        // Create the Posicao, which fails.

        restPosicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPosicaos() throws Exception {
        // Initialize the database
        insertedPosicao = posicaoRepository.saveAndFlush(posicao);

        // Get all the posicaoList
        restPosicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posicao.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }

    @Test
    @Transactional
    void getPosicao() throws Exception {
        // Initialize the database
        insertedPosicao = posicaoRepository.saveAndFlush(posicao);

        // Get the posicao
        restPosicaoMockMvc
            .perform(get(ENTITY_API_URL_ID, posicao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(posicao.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }

    @Test
    @Transactional
    void getNonExistingPosicao() throws Exception {
        // Get the posicao
        restPosicaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPosicao() throws Exception {
        // Initialize the database
        insertedPosicao = posicaoRepository.saveAndFlush(posicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the posicao
        Posicao updatedPosicao = posicaoRepository.findById(posicao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPosicao are not directly saved in db
        em.detach(updatedPosicao);
        updatedPosicao.codigo(UPDATED_CODIGO);

        restPosicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPosicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPosicao))
            )
            .andExpect(status().isOk());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPosicaoToMatchAllProperties(updatedPosicao);
    }

    @Test
    @Transactional
    void putNonExistingPosicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posicao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosicaoMockMvc
            .perform(put(ENTITY_API_URL_ID, posicao.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posicao)))
            .andExpect(status().isBadRequest());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(posicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosicaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePosicaoWithPatch() throws Exception {
        // Initialize the database
        insertedPosicao = posicaoRepository.saveAndFlush(posicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the posicao using partial update
        Posicao partialUpdatedPosicao = new Posicao();
        partialUpdatedPosicao.setId(posicao.getId());

        restPosicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosicao))
            )
            .andExpect(status().isOk());

        // Validate the Posicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPosicaoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPosicao, posicao), getPersistedPosicao(posicao));
    }

    @Test
    @Transactional
    void fullUpdatePosicaoWithPatch() throws Exception {
        // Initialize the database
        insertedPosicao = posicaoRepository.saveAndFlush(posicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the posicao using partial update
        Posicao partialUpdatedPosicao = new Posicao();
        partialUpdatedPosicao.setId(posicao.getId());

        partialUpdatedPosicao.codigo(UPDATED_CODIGO);

        restPosicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosicao))
            )
            .andExpect(status().isOk());

        // Validate the Posicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPosicaoUpdatableFieldsEquals(partialUpdatedPosicao, getPersistedPosicao(partialUpdatedPosicao));
    }

    @Test
    @Transactional
    void patchNonExistingPosicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posicao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, posicao.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(posicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(posicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosicaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(posicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Posicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosicao() throws Exception {
        // Initialize the database
        insertedPosicao = posicaoRepository.saveAndFlush(posicao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the posicao
        restPosicaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, posicao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return posicaoRepository.count();
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

    protected Posicao getPersistedPosicao(Posicao posicao) {
        return posicaoRepository.findById(posicao.getId()).orElseThrow();
    }

    protected void assertPersistedPosicaoToMatchAllProperties(Posicao expectedPosicao) {
        assertPosicaoAllPropertiesEquals(expectedPosicao, getPersistedPosicao(expectedPosicao));
    }

    protected void assertPersistedPosicaoToMatchUpdatableProperties(Posicao expectedPosicao) {
        assertPosicaoAllUpdatablePropertiesEquals(expectedPosicao, getPersistedPosicao(expectedPosicao));
    }
}
