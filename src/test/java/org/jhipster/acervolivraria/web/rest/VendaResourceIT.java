package org.jhipster.acervolivraria.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster.acervolivraria.domain.VendaAsserts.*;
import static org.jhipster.acervolivraria.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.jhipster.acervolivraria.IntegrationTest;
import org.jhipster.acervolivraria.domain.Venda;
import org.jhipster.acervolivraria.repository.VendaRepository;
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
 * Integration tests for the {@link VendaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendaResourceIT {

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final Float DEFAULT_PRECO_VENDA = 1F;
    private static final Float UPDATED_PRECO_VENDA = 2F;

    private static final Float DEFAULT_VALOR_TOTAL = 1F;
    private static final Float UPDATED_VALOR_TOTAL = 2F;

    private static final String ENTITY_API_URL = "/api/vendas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendaMockMvc;

    private Venda venda;

    private Venda insertedVenda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venda createEntity() {
        return new Venda().quantidade(DEFAULT_QUANTIDADE).precoVenda(DEFAULT_PRECO_VENDA).valorTotal(DEFAULT_VALOR_TOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venda createUpdatedEntity() {
        return new Venda().quantidade(UPDATED_QUANTIDADE).precoVenda(UPDATED_PRECO_VENDA).valorTotal(UPDATED_VALOR_TOTAL);
    }

    @BeforeEach
    public void initTest() {
        venda = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVenda != null) {
            vendaRepository.delete(insertedVenda);
            insertedVenda = null;
        }
    }

    @Test
    @Transactional
    void createVenda() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Venda
        var returnedVenda = om.readValue(
            restVendaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venda)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Venda.class
        );

        // Validate the Venda in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVendaUpdatableFieldsEquals(returnedVenda, getPersistedVenda(returnedVenda));

        insertedVenda = returnedVenda;
    }

    @Test
    @Transactional
    void createVendaWithExistingId() throws Exception {
        // Create the Venda with an existing ID
        venda.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venda)))
            .andExpect(status().isBadRequest());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venda.setQuantidade(null);

        // Create the Venda, which fails.

        restVendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venda)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecoVendaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venda.setPrecoVenda(null);

        // Create the Venda, which fails.

        restVendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venda)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendas() throws Exception {
        // Initialize the database
        insertedVenda = vendaRepository.saveAndFlush(venda);

        // Get all the vendaList
        restVendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venda.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].precoVenda").value(hasItem(DEFAULT_PRECO_VENDA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getVenda() throws Exception {
        // Initialize the database
        insertedVenda = vendaRepository.saveAndFlush(venda);

        // Get the venda
        restVendaMockMvc
            .perform(get(ENTITY_API_URL_ID, venda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venda.getId().intValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.precoVenda").value(DEFAULT_PRECO_VENDA.doubleValue()))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingVenda() throws Exception {
        // Get the venda
        restVendaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVenda() throws Exception {
        // Initialize the database
        insertedVenda = vendaRepository.saveAndFlush(venda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venda
        Venda updatedVenda = vendaRepository.findById(venda.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVenda are not directly saved in db
        em.detach(updatedVenda);
        updatedVenda.quantidade(UPDATED_QUANTIDADE).precoVenda(UPDATED_PRECO_VENDA).valorTotal(UPDATED_VALOR_TOTAL);

        restVendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVenda.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVenda))
            )
            .andExpect(status().isOk());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVendaToMatchAllProperties(updatedVenda);
    }

    @Test
    @Transactional
    void putNonExistingVenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venda.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendaMockMvc
            .perform(put(ENTITY_API_URL_ID, venda.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venda)))
            .andExpect(status().isBadRequest());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(venda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venda)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendaWithPatch() throws Exception {
        // Initialize the database
        insertedVenda = vendaRepository.saveAndFlush(venda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venda using partial update
        Venda partialUpdatedVenda = new Venda();
        partialUpdatedVenda.setId(venda.getId());

        restVendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenda))
            )
            .andExpect(status().isOk());

        // Validate the Venda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVenda, venda), getPersistedVenda(venda));
    }

    @Test
    @Transactional
    void fullUpdateVendaWithPatch() throws Exception {
        // Initialize the database
        insertedVenda = vendaRepository.saveAndFlush(venda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venda using partial update
        Venda partialUpdatedVenda = new Venda();
        partialUpdatedVenda.setId(venda.getId());

        partialUpdatedVenda.quantidade(UPDATED_QUANTIDADE).precoVenda(UPDATED_PRECO_VENDA).valorTotal(UPDATED_VALOR_TOTAL);

        restVendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenda))
            )
            .andExpect(status().isOk());

        // Validate the Venda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendaUpdatableFieldsEquals(partialUpdatedVenda, getPersistedVenda(partialUpdatedVenda));
    }

    @Test
    @Transactional
    void patchNonExistingVenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venda.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venda.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(venda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(venda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(venda)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenda() throws Exception {
        // Initialize the database
        insertedVenda = vendaRepository.saveAndFlush(venda);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the venda
        restVendaMockMvc
            .perform(delete(ENTITY_API_URL_ID, venda.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vendaRepository.count();
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

    protected Venda getPersistedVenda(Venda venda) {
        return vendaRepository.findById(venda.getId()).orElseThrow();
    }

    protected void assertPersistedVendaToMatchAllProperties(Venda expectedVenda) {
        assertVendaAllPropertiesEquals(expectedVenda, getPersistedVenda(expectedVenda));
    }

    protected void assertPersistedVendaToMatchUpdatableProperties(Venda expectedVenda) {
        assertVendaAllUpdatablePropertiesEquals(expectedVenda, getPersistedVenda(expectedVenda));
    }
}
