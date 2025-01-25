package org.jhipster.acervolivraria.service;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.acervolivraria.domain.Venda}.
 */
public interface VendaService {
    /**
     * Save a venda.
     *
     * @param venda the entity to save.
     * @return the persisted entity.
     */
    Venda save(Venda venda);

    /**
     * Updates a venda.
     *
     * @param venda the entity to update.
     * @return the persisted entity.
     */
    Venda update(Venda venda);

    /**
     * Partially updates a venda.
     *
     * @param venda the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Venda> partialUpdate(Venda venda);

    /**
     * Get all the vendas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Venda> findAll(Pageable pageable);

    /**
     * Get the "id" venda.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Venda> findOne(Long id);

    /**
     * Delete the "id" venda.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Verify if a venda its valid.
     * The verification is made by checking if the quantity of the book editions are enough to make the sale.
     *
     * @param venda the entity to verify.
     * @return a boolean indicating if the sale is valid.
     */
    boolean verifyVenda(Venda venda);

    /**
     * Get the sum of all vendas.
     *
     * @return the sum of all vendas.
     */
    Double getTotalSum();
}
