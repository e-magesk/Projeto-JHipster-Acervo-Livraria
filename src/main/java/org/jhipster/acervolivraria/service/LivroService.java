package org.jhipster.acervolivraria.service;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.acervolivraria.domain.Livro}.
 */
public interface LivroService {
    /**
     * Save a livro.
     *
     * @param livro the entity to save.
     * @return the persisted entity.
     */
    Livro save(Livro livro);

    /**
     * Updates a livro.
     *
     * @param livro the entity to update.
     * @return the persisted entity.
     */
    Livro update(Livro livro);

    /**
     * Partially updates a livro.
     *
     * @param livro the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Livro> partialUpdate(Livro livro);

    /**
     * Get all the livros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Livro> findAll(Pageable pageable);

    /**
     * Get all the livros with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Livro> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" livro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Livro> findOne(Long id);

    /**
     * Delete the "id" livro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
