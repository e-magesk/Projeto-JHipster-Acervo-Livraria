package org.jhipster.acervolivraria.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster.acervolivraria.domain.EdicaoAsserts.*;
import static org.jhipster.acervolivraria.web.rest.TestUtil.createUpdateProxyForBean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.jhipster.acervolivraria.IntegrationTest;
import org.jhipster.acervolivraria.domain.Edicao;
import org.jhipster.acervolivraria.repository.EdicaoRepository;
import org.jhipster.acervolivraria.service.EdicaoService;
import org.junit.jupiter.api.AfterEach;
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
 * Integration tests for the {@link EdicaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EdicaoResourceIT {

    private static final String DEFAULT_EDITORA = "AAAAAAAAAA";
    private static final String UPDATED_EDITORA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_LANCAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_LANCAMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_QUANTIDADE_EXEMPLARES = 1;
    private static final Integer UPDATED_QUANTIDADE_EXEMPLARES = 2;

    private static final Float DEFAULT_PRECO = 1F;
    private static final Float UPDATED_PRECO = 2F;

    private static final String ENTITY_API_URL = "/api/edicaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EdicaoRepository edicaoRepository;

    @Mock
    private EdicaoRepository edicaoRepositoryMock;

    @Mock
    private EdicaoService edicaoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEdicaoMockMvc;

    private Edicao edicao;

    private Edicao insertedEdicao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edicao createEntity() {
        return new Edicao()
            .editora(DEFAULT_EDITORA)
            .dataLancamento(DEFAULT_DATA_LANCAMENTO)
            .quantidadeExemplares(DEFAULT_QUANTIDADE_EXEMPLARES)
            .preco(DEFAULT_PRECO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edicao createUpdatedEntity() {
        return new Edicao()
            .editora(UPDATED_EDITORA)
            .dataLancamento(UPDATED_DATA_LANCAMENTO)
            .quantidadeExemplares(UPDATED_QUANTIDADE_EXEMPLARES)
            .preco(UPDATED_PRECO);
    }

    @BeforeEach
    public void initTest() {
        edicao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEdicao != null) {
            edicaoRepository.delete(insertedEdicao);
            insertedEdicao = null;
        }
    }

    @Test
    @Transactional
    void createEdicao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Edicao
        var returnedEdicao = om.readValue(
            restEdicaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Edicao.class
        );

        // Validate the Edicao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEdicaoUpdatableFieldsEquals(returnedEdicao, getPersistedEdicao(returnedEdicao));

        insertedEdicao = returnedEdicao;
    }

    @Test
    @Transactional
    void createEdicaoWithExistingId() throws Exception {
        // Create the Edicao with an existing ID
        edicao.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEditoraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edicao.setEditora(null);

        // Create the Edicao, which fails.

        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataLancamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edicao.setDataLancamento(null);

        // Create the Edicao, which fails.

        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantidadeExemplaresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edicao.setQuantidadeExemplares(null);

        // Create the Edicao, which fails.

        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edicao.setPreco(null);

        // Create the Edicao, which fails.

        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEdicaos() throws Exception {
        // Initialize the database
        insertedEdicao = edicaoRepository.saveAndFlush(edicao);

        // Get all the edicaoList
        restEdicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edicao.getId().intValue())))
            .andExpect(jsonPath("$.[*].editora").value(hasItem(DEFAULT_EDITORA)))
            .andExpect(jsonPath("$.[*].dataLancamento").value(hasItem(DEFAULT_DATA_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].quantidadeExemplares").value(hasItem(DEFAULT_QUANTIDADE_EXEMPLARES)))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEdicaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(edicaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEdicaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(edicaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEdicaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(edicaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEdicaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(edicaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEdicao() throws Exception {
        // Initialize the database
        insertedEdicao = edicaoRepository.saveAndFlush(edicao);

        // Get the edicao
        restEdicaoMockMvc
            .perform(get(ENTITY_API_URL_ID, edicao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(edicao.getId().intValue()))
            .andExpect(jsonPath("$.editora").value(DEFAULT_EDITORA))
            .andExpect(jsonPath("$.dataLancamento").value(DEFAULT_DATA_LANCAMENTO.toString()))
            .andExpect(jsonPath("$.quantidadeExemplares").value(DEFAULT_QUANTIDADE_EXEMPLARES))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEdicao() throws Exception {
        // Get the edicao
        restEdicaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEdicao() throws Exception {
        // Initialize the database
        insertedEdicao = edicaoRepository.saveAndFlush(edicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edicao
        Edicao updatedEdicao = edicaoRepository.findById(edicao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEdicao are not directly saved in db
        em.detach(updatedEdicao);
        updatedEdicao
            .editora(UPDATED_EDITORA)
            .dataLancamento(UPDATED_DATA_LANCAMENTO)
            .quantidadeExemplares(UPDATED_QUANTIDADE_EXEMPLARES)
            .preco(UPDATED_PRECO);

        restEdicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEdicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEdicao))
            )
            .andExpect(status().isOk());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEdicaoToMatchAllProperties(updatedEdicao);
    }

    @Test
    @Transactional
    void putNonExistingEdicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edicao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(put(ENTITY_API_URL_ID, edicao.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEdicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEdicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEdicaoWithPatch() throws Exception {
        // Initialize the database
        insertedEdicao = edicaoRepository.saveAndFlush(edicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edicao using partial update
        Edicao partialUpdatedEdicao = new Edicao();
        partialUpdatedEdicao.setId(edicao.getId());

        partialUpdatedEdicao.editora(UPDATED_EDITORA).preco(UPDATED_PRECO);

        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdicao))
            )
            .andExpect(status().isOk());

        // Validate the Edicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdicaoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEdicao, edicao), getPersistedEdicao(edicao));
    }

    @Test
    @Transactional
    void fullUpdateEdicaoWithPatch() throws Exception {
        // Initialize the database
        insertedEdicao = edicaoRepository.saveAndFlush(edicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edicao using partial update
        Edicao partialUpdatedEdicao = new Edicao();
        partialUpdatedEdicao.setId(edicao.getId());

        partialUpdatedEdicao
            .editora(UPDATED_EDITORA)
            .dataLancamento(UPDATED_DATA_LANCAMENTO)
            .quantidadeExemplares(UPDATED_QUANTIDADE_EXEMPLARES)
            .preco(UPDATED_PRECO);

        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdicao))
            )
            .andExpect(status().isOk());

        // Validate the Edicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdicaoUpdatableFieldsEquals(partialUpdatedEdicao, getPersistedEdicao(partialUpdatedEdicao));
    }

    @Test
    @Transactional
    void patchNonExistingEdicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edicao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, edicao.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEdicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEdicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edicao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(edicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEdicao() throws Exception {
        // Initialize the database
        insertedEdicao = edicaoRepository.saveAndFlush(edicao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the edicao
        restEdicaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, edicao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return edicaoRepository.count();
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

    protected Edicao getPersistedEdicao(Edicao edicao) {
        return edicaoRepository.findById(edicao.getId()).orElseThrow();
    }

    protected void assertPersistedEdicaoToMatchAllProperties(Edicao expectedEdicao) {
        assertEdicaoAllPropertiesEquals(expectedEdicao, getPersistedEdicao(expectedEdicao));
    }

    protected void assertPersistedEdicaoToMatchUpdatableProperties(Edicao expectedEdicao) {
        assertEdicaoAllUpdatablePropertiesEquals(expectedEdicao, getPersistedEdicao(expectedEdicao));
    }
}
