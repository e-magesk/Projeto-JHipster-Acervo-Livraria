package org.jhipster.acervolivraria.service.impl;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Venda;
import org.jhipster.acervolivraria.repository.VendaRepository;
import org.jhipster.acervolivraria.service.VendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster.acervolivraria.domain.Venda}.
 */
@Service
@Transactional
public class VendaServiceImpl implements VendaService {

    private static final Logger LOG = LoggerFactory.getLogger(VendaServiceImpl.class);

    private final VendaRepository vendaRepository;

    public VendaServiceImpl(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Override
    public Venda save(Venda venda) {
        LOG.debug("Request to save Venda : {}", venda);
        return vendaRepository.save(venda);
    }

    @Override
    public Venda update(Venda venda) {
        LOG.debug("Request to update Venda : {}", venda);
        return vendaRepository.save(venda);
    }

    @Override
    public Optional<Venda> partialUpdate(Venda venda) {
        LOG.debug("Request to partially update Venda : {}", venda);

        return vendaRepository
            .findById(venda.getId())
            .map(existingVenda -> {
                if (venda.getQuantidade() != null) {
                    existingVenda.setQuantidade(venda.getQuantidade());
                }
                if (venda.getPrecoVenda() != null) {
                    existingVenda.setPrecoVenda(venda.getPrecoVenda());
                }
                if (venda.getValorTotal() != null) {
                    existingVenda.setValorTotal(venda.getValorTotal());
                }

                return existingVenda;
            })
            .map(vendaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Venda> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vendas");
        return vendaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venda> findOne(Long id) {
        LOG.debug("Request to get Venda : {}", id);
        return vendaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Venda : {}", id);
        vendaRepository.deleteById(id);
    }
}
