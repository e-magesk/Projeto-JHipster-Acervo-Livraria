package org.jhipster.acervolivraria.service;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.acervolivraria.domain.Compra}.
 */
public interface CompraService {
    /**
     * Save a compra.
     *
     * @param compra the entity to save.
     * @return the persisted entity.
     */
    Compra save(Compra compra);

    /**
     * Updates a compra.
     *
     * @param compra the entity to update.
     * @return the persisted entity.
     */
    Compra update(Compra compra);

    /**
     * Partially updates a compra.
     *
     * @param compra the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Compra> partialUpdate(Compra compra);

    /**
     * Get all the compras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Compra> findAll(Pageable pageable);

    /**
     * Get the "id" compra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Compra> findOne(Long id);

    /**
     * Delete the "id" compra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the sum of all compras.
     *
     * @return the sum of all compras.
     */
    Double getTotalSum();
}
