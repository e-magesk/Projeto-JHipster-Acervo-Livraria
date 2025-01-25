package org.jhipster.acervolivraria.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster.acervolivraria.domain.CompraAsserts.*;
import static org.jhipster.acervolivraria.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.jhipster.acervolivraria.IntegrationTest;
import org.jhipster.acervolivraria.domain.Compra;
import org.jhipster.acervolivraria.repository.CompraRepository;
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
 * Integration tests for the {@link CompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompraResourceIT {

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final Float DEFAULT_PRECO_COMPRA = 1F;
    private static final Float UPDATED_PRECO_COMPRA = 2F;

    private static final Float DEFAULT_VALOR_TOTAL = 1F;
    private static final Float UPDATED_VALOR_TOTAL = 2F;

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraMockMvc;

    private Compra compra;

    private Compra insertedCompra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createEntity() {
        return new Compra().quantidade(DEFAULT_QUANTIDADE).precoCompra(DEFAULT_PRECO_COMPRA).valorTotal(DEFAULT_VALOR_TOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createUpdatedEntity() {
        return new Compra().quantidade(UPDATED_QUANTIDADE).precoCompra(UPDATED_PRECO_COMPRA).valorTotal(UPDATED_VALOR_TOTAL);
    }

    @BeforeEach
    public void initTest() {
        compra = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompra != null) {
            compraRepository.delete(insertedCompra);
            insertedCompra = null;
        }
    }

    @Test
    @Transactional
    void createCompra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compra
        var returnedCompra = om.readValue(
            restCompraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compra)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Compra.class
        );

        // Validate the Compra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCompraUpdatableFieldsEquals(returnedCompra, getPersistedCompra(returnedCompra));

        insertedCompra = returnedCompra;
    }

    @Test
    @Transactional
    void createCompraWithExistingId() throws Exception {
        // Create the Compra with an existing ID
        compra.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compra)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compra.setQuantidade(null);

        // Create the Compra, which fails.

        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compra)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecoCompraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compra.setPrecoCompra(null);

        // Create the Compra, which fails.

        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compra)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].precoCompra").value(hasItem(DEFAULT_PRECO_COMPRA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getCompra() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get the compra
        restCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, compra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compra.getId().intValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.precoCompra").value(DEFAULT_PRECO_COMPRA.doubleValue()))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompra() throws Exception {
        // Get the compra
        restCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompra() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compra
        Compra updatedCompra = compraRepository.findById(compra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompra are not directly saved in db
        em.detach(updatedCompra);
        updatedCompra.quantidade(UPDATED_QUANTIDADE).precoCompra(UPDATED_PRECO_COMPRA).valorTotal(UPDATED_VALOR_TOTAL);

        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompraToMatchAllProperties(updatedCompra);
    }

    @Test
    @Transactional
    void putNonExistingCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL_ID, compra.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compra)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra.precoCompra(UPDATED_PRECO_COMPRA).valorTotal(UPDATED_VALOR_TOTAL);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompraUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompra, compra), getPersistedCompra(compra));
    }

    @Test
    @Transactional
    void fullUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra.quantidade(UPDATED_QUANTIDADE).precoCompra(UPDATED_PRECO_COMPRA).valorTotal(UPDATED_VALOR_TOTAL);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompraUpdatableFieldsEquals(partialUpdatedCompra, getPersistedCompra(partialUpdatedCompra));
    }

    @Test
    @Transactional
    void patchNonExistingCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compra.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompra() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compra
        restCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, compra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compraRepository.count();
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

    protected Compra getPersistedCompra(Compra compra) {
        return compraRepository.findById(compra.getId()).orElseThrow();
    }

    protected void assertPersistedCompraToMatchAllProperties(Compra expectedCompra) {
        assertCompraAllPropertiesEquals(expectedCompra, getPersistedCompra(expectedCompra));
    }

    protected void assertPersistedCompraToMatchUpdatableProperties(Compra expectedCompra) {
        assertCompraAllUpdatablePropertiesEquals(expectedCompra, getPersistedCompra(expectedCompra));
    }
}
