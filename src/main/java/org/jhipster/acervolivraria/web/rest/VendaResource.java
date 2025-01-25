package org.jhipster.acervolivraria.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster.acervolivraria.domain.Venda;
import org.jhipster.acervolivraria.repository.VendaRepository;
import org.jhipster.acervolivraria.service.VendaService;
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
 * REST controller for managing {@link org.jhipster.acervolivraria.domain.Venda}.
 */
@RestController
@RequestMapping("/api/vendas")
public class VendaResource {

    private static final Logger LOG = LoggerFactory.getLogger(VendaResource.class);

    private static final String ENTITY_NAME = "venda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendaService vendaService;

    private final VendaRepository vendaRepository;

    public VendaResource(VendaService vendaService, VendaRepository vendaRepository) {
        this.vendaService = vendaService;
        this.vendaRepository = vendaRepository;
    }

    /**
     * {@code POST  /vendas} : Create a new venda.
     *
     * @param venda the venda to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new venda, or with status {@code 400 (Bad Request)} if the venda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Venda> createVenda(@Valid @RequestBody Venda venda) throws URISyntaxException {
        LOG.debug("REST request to save Venda : {}", venda);
        if (venda.getId() != null) {
            throw new BadRequestAlertException("A new venda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (!vendaService.verifyVenda(venda)) {
            throw new BadRequestAlertException(
                "Venda inválida",
                ENTITY_NAME,
                "Venda inválida pois a quantidade de edições disponíveis é insuficiente. <br> Limite disponível: " +
                venda.getEdicao().getQuantidadeExemplares()
            );
        }
        venda = vendaService.save(venda);
        return ResponseEntity.created(new URI("/api/vendas/" + venda.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, venda.getId().toString()))
            .body(venda);
    }

    /**
     * {@code PUT  /vendas/:id} : Updates an existing venda.
     *
     * @param id the id of the venda to save.
     * @param venda the venda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venda,
     * or with status {@code 400 (Bad Request)} if the venda is not valid,
     * or with status {@code 500 (Internal Server Error)} if the venda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Venda> updateVenda(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Venda venda)
        throws URISyntaxException {
        LOG.debug("REST request to update Venda : {}, {}", id, venda);
        if (venda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        venda = vendaService.update(venda);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venda.getId().toString()))
            .body(venda);
    }

    /**
     * {@code PATCH  /vendas/:id} : Partial updates given fields of an existing venda, field will ignore if it is null
     *
     * @param id the id of the venda to save.
     * @param venda the venda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venda,
     * or with status {@code 400 (Bad Request)} if the venda is not valid,
     * or with status {@code 404 (Not Found)} if the venda is not found,
     * or with status {@code 500 (Internal Server Error)} if the venda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Venda> partialUpdateVenda(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Venda venda
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Venda partially : {}, {}", id, venda);
        if (venda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Venda> result = vendaService.partialUpdate(venda);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venda.getId().toString())
        );
    }

    /**
     * {@code GET  /vendas} : get all the vendas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Venda>> getAllVendas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Vendas");
        Page<Venda> page = vendaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vendas/:id} : get the "id" venda.
     *
     * @param id the id of the venda to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the venda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVenda(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Venda : {}", id);
        Optional<Venda> venda = vendaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venda);
    }

    /**
     * {@code DELETE  /vendas/:id} : delete the "id" venda.
     *
     * @param id the id of the venda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenda(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Venda : {}", id);
        vendaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
