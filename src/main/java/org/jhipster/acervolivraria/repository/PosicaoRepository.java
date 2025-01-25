package org.jhipster.acervolivraria.repository;

import org.jhipster.acervolivraria.domain.Posicao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Posicao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PosicaoRepository extends JpaRepository<Posicao, Long> {}
