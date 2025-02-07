package org.jhipster.acervolivraria.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Edicao;
import org.jhipster.acervolivraria.repository.EdicaoRepository;
import org.jhipster.acervolivraria.service.EdicaoService;
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
 * REST controller for managing {@link org.jhipster.acervolivraria.domain.Edicao}.
 */
@RestController
@RequestMapping("/api/edicaos")
public class EdicaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EdicaoResource.class);

    private static final String ENTITY_NAME = "edicao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EdicaoService edicaoService;

    private final EdicaoRepository edicaoRepository;

    public EdicaoResource(EdicaoService edicaoService, EdicaoRepository edicaoRepository) {
        this.edicaoService = edicaoService;
        this.edicaoRepository = edicaoRepository;
    }

    /**
     * {@code POST  /edicaos} : Create a new edicao.
     *
     * @param edicao the edicao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new edicao, or with status {@code 400 (Bad Request)} if the edicao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Edicao> createEdicao(@Valid @RequestBody Edicao edicao) throws URISyntaxException {
        LOG.debug("REST request to save Edicao : {}", edicao);
        if (edicao.getId() != null) {
            throw new BadRequestAlertException("A new edicao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        edicao = edicaoService.save(edicao);
        return ResponseEntity.created(new URI("/api/edicaos/" + edicao.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, edicao.getId().toString()))
            .body(edicao);
    }

    /**
     * {@code PUT  /edicaos/:id} : Updates an existing edicao.
     *
     * @param id the id of the edicao to save.
     * @param edicao the edicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edicao,
     * or with status {@code 400 (Bad Request)} if the edicao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the edicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Edicao> updateEdicao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Edicao edicao
    ) throws URISyntaxException {
        LOG.debug("REST request to update Edicao : {}, {}", id, edicao);
        if (edicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        edicao = edicaoService.update(edicao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edicao.getId().toString()))
            .body(edicao);
    }

    /**
     * {@code PATCH  /edicaos/:id} : Partial updates given fields of an existing edicao, field will ignore if it is null
     *
     * @param id the id of the edicao to save.
     * @param edicao the edicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edicao,
     * or with status {@code 400 (Bad Request)} if the edicao is not valid,
     * or with status {@code 404 (Not Found)} if the edicao is not found,
     * or with status {@code 500 (Internal Server Error)} if the edicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Edicao> partialUpdateEdicao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Edicao edicao
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Edicao partially : {}, {}", id, edicao);
        if (edicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Edicao> result = edicaoService.partialUpdate(edicao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edicao.getId().toString())
        );
    }

    /**
     * {@code GET  /edicaos} : get all the edicaos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of edicaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Edicao>> getAllEdicaos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Edicaos");
        Page<Edicao> page;
        if (eagerload) {
            page = edicaoService.findAllWithEagerRelationships(pageable);
        } else {
            page = edicaoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /edicaos/:id} : get the "id" edicao.
     *
     * @param id the id of the edicao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the edicao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Edicao> getEdicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Edicao : {}", id);
        Optional<Edicao> edicao = edicaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(edicao);
    }

    /**
     * {@code DELETE  /edicaos/:id} : delete the "id" edicao.
     *
     * @param id the id of the edicao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEdicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Edicao : {}", id);
        edicaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
