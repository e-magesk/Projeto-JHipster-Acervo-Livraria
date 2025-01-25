package org.jhipster.acervolivraria.repository;

import java.util.List;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Autor entity.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    default Optional<Autor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Autor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Autor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select autor from Autor autor left join fetch autor.livro", countQuery = "select count(autor) from Autor autor")
    Page<Autor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select autor from Autor autor left join fetch autor.livro")
    List<Autor> findAllWithToOneRelationships();

    @Query("select autor from Autor autor left join fetch autor.livro where autor.id =:id")
    Optional<Autor> findOneWithToOneRelationships(@Param("id") Long id);
}
