package org.jhipster.acervolivraria.service.impl;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Compra;
import org.jhipster.acervolivraria.repository.CompraRepository;
import org.jhipster.acervolivraria.service.CompraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster.acervolivraria.domain.Compra}.
 */
@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private static final Logger LOG = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;

    public CompraServiceImpl(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public Compra save(Compra compra) {
        LOG.debug("Request to save Compra : {}", compra);
        return compraRepository.save(compra);
    }

    @Override
    public Compra update(Compra compra) {
        LOG.debug("Request to update Compra : {}", compra);
        return compraRepository.save(compra);
    }

    @Override
    public Optional<Compra> partialUpdate(Compra compra) {
        LOG.debug("Request to partially update Compra : {}", compra);

        return compraRepository
            .findById(compra.getId())
            .map(existingCompra -> {
                if (compra.getQuantidade() != null) {
                    existingCompra.setQuantidade(compra.getQuantidade());
                }
                if (compra.getPrecoCompra() != null) {
                    existingCompra.setPrecoCompra(compra.getPrecoCompra());
                }
                if (compra.getValorTotal() != null) {
                    existingCompra.setValorTotal(compra.getValorTotal());
                }

                return existingCompra;
            })
            .map(compraRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Compra> findAll(Pageable pageable) {
        LOG.debug("Request to get all Compras");
        return compraRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Compra> findOne(Long id) {
        LOG.debug("Request to get Compra : {}", id);
        return compraRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Compra : {}", id);
        compraRepository.deleteById(id);
    }
}
