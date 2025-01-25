package org.jhipster.acervolivraria.service.impl;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Edicao;
import org.jhipster.acervolivraria.repository.EdicaoRepository;
import org.jhipster.acervolivraria.service.EdicaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster.acervolivraria.domain.Edicao}.
 */
@Service
@Transactional
public class EdicaoServiceImpl implements EdicaoService {

    private static final Logger LOG = LoggerFactory.getLogger(EdicaoServiceImpl.class);

    private final EdicaoRepository edicaoRepository;

    public EdicaoServiceImpl(EdicaoRepository edicaoRepository) {
        this.edicaoRepository = edicaoRepository;
    }

    @Override
    public Edicao save(Edicao edicao) {
        LOG.debug("Request to save Edicao : {}", edicao);
        return edicaoRepository.save(edicao);
    }

    @Override
    public Edicao update(Edicao edicao) {
        LOG.debug("Request to update Edicao : {}", edicao);
        return edicaoRepository.save(edicao);
    }

    @Override
    public Optional<Edicao> partialUpdate(Edicao edicao) {
        LOG.debug("Request to partially update Edicao : {}", edicao);

        return edicaoRepository
            .findById(edicao.getId())
            .map(existingEdicao -> {
                if (edicao.getEditora() != null) {
                    existingEdicao.setEditora(edicao.getEditora());
                }
                if (edicao.getDataLancamento() != null) {
                    existingEdicao.setDataLancamento(edicao.getDataLancamento());
                }
                if (edicao.getQuantidadeExemplares() != null) {
                    existingEdicao.setQuantidadeExemplares(edicao.getQuantidadeExemplares());
                }
                if (edicao.getPreco() != null) {
                    existingEdicao.setPreco(edicao.getPreco());
                }

                return existingEdicao;
            })
            .map(edicaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Edicao> findAll(Pageable pageable) {
        LOG.debug("Request to get all Edicaos");
        return edicaoRepository.findAll(pageable);
    }

    public Page<Edicao> findAllWithEagerRelationships(Pageable pageable) {
        return edicaoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Edicao> findOne(Long id) {
        LOG.debug("Request to get Edicao : {}", id);
        return edicaoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Edicao : {}", id);
        edicaoRepository.deleteById(id);
    }
}
