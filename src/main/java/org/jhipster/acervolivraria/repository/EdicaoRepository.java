package org.jhipster.acervolivraria.repository;

import java.util.List;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Edicao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Edicao entity.
 */
@Repository
public interface EdicaoRepository extends JpaRepository<Edicao, Long> {
    default Optional<Edicao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Edicao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Edicao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select edicao from Edicao edicao left join fetch edicao.posicao left join fetch edicao.livro",
        countQuery = "select count(edicao) from Edicao edicao"
    )
    Page<Edicao> findAllWithToOneRelationships(Pageable pageable);

    @Query("select edicao from Edicao edicao left join fetch edicao.posicao left join fetch edicao.livro")
    List<Edicao> findAllWithToOneRelationships();

    @Query("select edicao from Edicao edicao left join fetch edicao.posicao left join fetch edicao.livro where edicao.id =:id")
    Optional<Edicao> findOneWithToOneRelationships(@Param("id") Long id);
}
