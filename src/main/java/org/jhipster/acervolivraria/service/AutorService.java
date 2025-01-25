package org.jhipster.acervolivraria.service;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.acervolivraria.domain.Autor}.
 */
public interface AutorService {
    /**
     * Save a autor.
     *
     * @param autor the entity to save.
     * @return the persisted entity.
     */
    Autor save(Autor autor);

    /**
     * Updates a autor.
     *
     * @param autor the entity to update.
     * @return the persisted entity.
     */
    Autor update(Autor autor);

    /**
     * Partially updates a autor.
     *
     * @param autor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Autor> partialUpdate(Autor autor);

    /**
     * Get all the autors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Autor> findAll(Pageable pageable);

    /**
     * Get all the autors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Autor> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" autor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Autor> findOne(Long id);

    /**
     * Delete the "id" autor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
