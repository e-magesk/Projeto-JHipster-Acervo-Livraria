package org.jhipster.acervolivraria.service;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Edicao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.acervolivraria.domain.Edicao}.
 */
public interface EdicaoService {
    /**
     * Save a edicao.
     *
     * @param edicao the entity to save.
     * @return the persisted entity.
     */
    Edicao save(Edicao edicao);

    /**
     * Updates a edicao.
     *
     * @param edicao the entity to update.
     * @return the persisted entity.
     */
    Edicao update(Edicao edicao);

    /**
     * Partially updates a edicao.
     *
     * @param edicao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Edicao> partialUpdate(Edicao edicao);

    /**
     * Get all the edicaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Edicao> findAll(Pageable pageable);

    /**
     * Get all the edicaos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Edicao> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" edicao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Edicao> findOne(Long id);

    /**
     * Delete the "id" edicao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
