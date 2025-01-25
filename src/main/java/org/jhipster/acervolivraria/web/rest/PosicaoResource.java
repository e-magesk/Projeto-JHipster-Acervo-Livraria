package org.jhipster.acervolivraria.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Posicao;
import org.jhipster.acervolivraria.repository.PosicaoRepository;
import org.jhipster.acervolivraria.service.PosicaoService;
import org.jhipster.acervolivraria.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.jhipster.acervolivraria.domain.Posicao}.
 */
@RestController
@RequestMapping("/api/posicaos")
public class PosicaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(PosicaoResource.class);

    private static final String ENTITY_NAME = "posicao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PosicaoService posicaoService;

    private final PosicaoRepository posicaoRepository;

    public PosicaoResource(PosicaoService posicaoService, PosicaoRepository posicaoRepository) {
        this.posicaoService = posicaoService;
        this.posicaoRepository = posicaoRepository;
    }

    /**
     * {@code POST  /posicaos} : Create a new posicao.
     *
     * @param posicao the posicao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posicao, or with status {@code 400 (Bad Request)} if the posicao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Posicao> createPosicao(@Valid @RequestBody Posicao posicao) throws URISyntaxException {
        LOG.debug("REST request to save Posicao : {}", posicao);
        if (posicao.getId() != null) {
            throw new BadRequestAlertException("A new posicao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        posicao = posicaoService.save(posicao);
        return ResponseEntity.created(new URI("/api/posicaos/" + posicao.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, posicao.getId().toString()))
            .body(posicao);
    }

    /**
     * {@code PUT  /posicaos/:id} : Updates an existing posicao.
     *
     * @param id the id of the posicao to save.
     * @param posicao the posicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posicao,
     * or with status {@code 400 (Bad Request)} if the posicao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the posicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Posicao> updatePosicao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Posicao posicao
    ) throws URISyntaxException {
        LOG.debug("REST request to update Posicao : {}, {}", id, posicao);
        if (posicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, posicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!posicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        posicao = posicaoService.update(posicao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posicao.getId().toString()))
            .body(posicao);
    }

    /**
     * {@code PATCH  /posicaos/:id} : Partial updates given fields of an existing posicao, field will ignore if it is null
     *
     * @param id the id of the posicao to save.
     * @param posicao the posicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posicao,
     * or with status {@code 400 (Bad Request)} if the posicao is not valid,
     * or with status {@code 404 (Not Found)} if the posicao is not found,
     * or with status {@code 500 (Internal Server Error)} if the posicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Posicao> partialUpdatePosicao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Posicao posicao
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Posicao partially : {}, {}", id, posicao);
        if (posicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, posicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!posicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Posicao> result = posicaoService.partialUpdate(posicao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posicao.getId().toString())
        );
    }

    /**
     * {@code GET  /posicaos} : get all the posicaos.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posicaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Posicao>> getAllPosicaos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("edicao-is-null".equals(filter)) {
            LOG.debug("REST request to get all Posicaos where edicao is null");
            return new ResponseEntity<>(posicaoService.findAllWhereEdicaoIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of Posicaos");
        Page<Posicao> page = posicaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posicaos/:id} : get the "id" posicao.
     *
     * @param id the id of the posicao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posicao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Posicao> getPosicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Posicao : {}", id);
        Optional<Posicao> posicao = posicaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(posicao);
    }

    /**
     * {@code DELETE  /posicaos/:id} : delete the "id" posicao.
     *
     * @param id the id of the posicao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosicao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Posicao : {}", id);
        posicaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
