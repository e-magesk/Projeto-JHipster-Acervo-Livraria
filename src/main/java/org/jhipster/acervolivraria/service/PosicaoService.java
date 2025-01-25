package org.jhipster.acervolivraria.service;

import java.util.List;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Posicao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.acervolivraria.domain.Posicao}.
 */
public interface PosicaoService {
    /**
     * Save a posicao.
     *
     * @param posicao the entity to save.
     * @return the persisted entity.
     */
    Posicao save(Posicao posicao);

    /**
     * Updates a posicao.
     *
     * @param posicao the entity to update.
     * @return the persisted entity.
     */
    Posicao update(Posicao posicao);

    /**
     * Partially updates a posicao.
     *
     * @param posicao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Posicao> partialUpdate(Posicao posicao);

    /**
     * Get all the posicaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Posicao> findAll(Pageable pageable);

    /**
     * Get all the Posicao where Edicao is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Posicao> findAllWhereEdicaoIsNull();

    /**
     * Get the "id" posicao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Posicao> findOne(Long id);

    /**
     * Delete the "id" posicao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
