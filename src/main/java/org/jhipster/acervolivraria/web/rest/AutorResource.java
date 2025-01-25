package org.jhipster.acervolivraria.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Autor;
import org.jhipster.acervolivraria.domain.Livro;
import org.jhipster.acervolivraria.repository.AutorRepository;
import org.jhipster.acervolivraria.service.AutorService;
import org.jhipster.acervolivraria.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.jhipster.acervolivraria.domain.Autor}.
 */
@RestController
@RequestMapping("/api/autors")
public class AutorResource {

    private static final Logger LOG = LoggerFactory.getLogger(AutorResource.class);

    private static final String ENTITY_NAME = "autor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutorService autorService;

    private final AutorRepository autorRepository;

    public AutorResource(AutorService autorService, AutorRepository autorRepository) {
        this.autorService = autorService;
        this.autorRepository = autorRepository;
    }

    /**
     * {@code POST  /autors} : Create a new autor.
     *
     * @param autor the autor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autor, or with status {@code 400 (Bad Request)} if the autor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Autor> createAutor(@RequestBody Autor autor) throws URISyntaxException {
        LOG.debug("REST request to save Autor : {}", autor);
        if (autor.getId() != null) {
            throw new BadRequestAlertException("A new autor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        autor = autorService.save(autor);
        return ResponseEntity.created(new URI("/api/autors/" + autor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, autor.getId().toString()))
            .body(autor);
    }

    /**
     * {@code PUT  /autors/:id} : Updates an existing autor.
     *
     * @param id the id of the autor to save.
     * @param autor the autor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autor,
     * or with status {@code 400 (Bad Request)} if the autor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Autor> updateAutor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Autor autor)
        throws URISyntaxException {
        LOG.debug("REST request to update Autor : {}, {}", id, autor);
        if (autor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        autor = autorService.update(autor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autor.getId().toString()))
            .body(autor);
    }

    /**
     * {@code PATCH  /autors/:id} : Partial updates given fields of an existing autor, field will ignore if it is null
     *
     * @param id the id of the autor to save.
     * @param autor the autor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autor,
     * or with status {@code 400 (Bad Request)} if the autor is not valid,
     * or with status {@code 404 (Not Found)} if the autor is not found,
     * or with status {@code 500 (Internal Server Error)} if the autor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Autor> partialUpdateAutor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Autor autor)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Autor partially : {}, {}", id, autor);
        if (autor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Autor> result = autorService.partialUpdate(autor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autor.getId().toString())
        );
    }

    /**
     * {@code GET  /autors} : get all the autors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Autor>> getAllAutors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Autors");
        Page<Autor> page = autorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autors/:id} : get the "id" autor.
     *
     * @param id the id of the autor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Autor> getAutor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Autor : {}", id);
        Optional<Autor> autor = autorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autor);
    }

    @Transactional
    @GetMapping("/{id}/livros")
    public ResponseEntity<List<Livro>> getLivros(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Autor Livros : {}", id);
        Optional<Autor> autor = autorService.findOne(id);
        if (autor.isPresent()) {
            return ResponseEntity.ok().body(autor.get().getLivros().stream().toList());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * {@code DELETE  /autors/:id} : delete the "id" autor.
     *
     * @param id the id of the autor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Autor : {}", id);
        autorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
