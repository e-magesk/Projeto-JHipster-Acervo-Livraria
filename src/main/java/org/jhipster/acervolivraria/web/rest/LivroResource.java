package org.jhipster.acervolivraria.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Livro;
import org.jhipster.acervolivraria.repository.LivroRepository;
import org.jhipster.acervolivraria.service.LivroService;
import org.jhipster.acervolivraria.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.jhipster.acervolivraria.domain.Livro}.
 */
@RestController
@RequestMapping("/api/livros")
public class LivroResource {

    private static final Logger LOG = LoggerFactory.getLogger(LivroResource.class);

    private static final String ENTITY_NAME = "livro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivroService livroService;

    private final LivroRepository livroRepository;

    public LivroResource(LivroService livroService, LivroRepository livroRepository) {
        this.livroService = livroService;
        this.livroRepository = livroRepository;
    }

    /**
     * {@code POST  /livros} : Create a new livro.
     *
     * @param livro the livro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livro, or with status {@code 400 (Bad Request)} if the livro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Livro> createLivro(@Valid @RequestBody Livro livro) throws URISyntaxException {
        LOG.debug("REST request to save Livro : {}", livro);
        if (livro.getId() != null) {
            throw new BadRequestAlertException("A new livro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        livro = livroService.save(livro);
        return ResponseEntity.created(new URI("/api/livros/" + livro.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, livro.getId().toString()))
            .body(livro);
    }

    /**
     * {@code PUT  /livros/:id} : Updates an existing livro.
     *
     * @param id the id of the livro to save.
     * @param livro the livro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livro,
     * or with status {@code 400 (Bad Request)} if the livro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Livro> updateLivro(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Livro livro)
        throws URISyntaxException {
        LOG.debug("REST request to update Livro : {}, {}", id, livro);
        if (livro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        livro = livroService.update(livro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livro.getId().toString()))
            .body(livro);
    }

    /**
     * {@code PATCH  /livros/:id} : Partial updates given fields of an existing livro, field will ignore if it is null
     *
     * @param id the id of the livro to save.
     * @param livro the livro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livro,
     * or with status {@code 400 (Bad Request)} if the livro is not valid,
     * or with status {@code 404 (Not Found)} if the livro is not found,
     * or with status {@code 500 (Internal Server Error)} if the livro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Livro> partialUpdateLivro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Livro livro
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Livro partially : {}, {}", id, livro);
        if (livro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Livro> result = livroService.partialUpdate(livro);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livro.getId().toString())
        );
    }

    /**
     * {@code GET  /livros} : get all the livros.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Livro>> getAllLivros(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Livros");
        Page<Livro> page;
        if (eagerload) {
            page = livroService.findAllWithEagerRelationships(pageable);
        } else {
            page = livroService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /livros/:id} : get the "id" livro.
     *
     * @param id the id of the livro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivro(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Livro : {}", id);
        Optional<Livro> livro = livroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livro);
    }

    /**
     * {@code DELETE  /livros/:id} : delete the "id" livro.
     *
     * @param id the id of the livro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Livro : {}", id);
        livroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
