package org.jhipster.acervolivraria.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.jhipster.acervolivraria.domain.Posicao;
import org.jhipster.acervolivraria.repository.PosicaoRepository;
import org.jhipster.acervolivraria.service.PosicaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster.acervolivraria.domain.Posicao}.
 */
@Service
@Transactional
public class PosicaoServiceImpl implements PosicaoService {

    private static final Logger LOG = LoggerFactory.getLogger(PosicaoServiceImpl.class);

    private final PosicaoRepository posicaoRepository;

    public PosicaoServiceImpl(PosicaoRepository posicaoRepository) {
        this.posicaoRepository = posicaoRepository;
    }

    @Override
    public Posicao save(Posicao posicao) {
        LOG.debug("Request to save Posicao : {}", posicao);
        return posicaoRepository.save(posicao);
    }

    @Override
    public Posicao update(Posicao posicao) {
        LOG.debug("Request to update Posicao : {}", posicao);
        return posicaoRepository.save(posicao);
    }

    @Override
    public Optional<Posicao> partialUpdate(Posicao posicao) {
        LOG.debug("Request to partially update Posicao : {}", posicao);

        return posicaoRepository
            .findById(posicao.getId())
            .map(existingPosicao -> {
                if (posicao.getCodigo() != null) {
                    existingPosicao.setCodigo(posicao.getCodigo());
                }

                return existingPosicao;
            })
            .map(posicaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Posicao> findAll(Pageable pageable) {
        LOG.debug("Request to get all Posicaos");
        return posicaoRepository.findAll(pageable);
    }

    /**
     *  Get all the posicaos where Edicao is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Posicao> findAllWhereEdicaoIsNull() {
        LOG.debug("Request to get all posicaos where Edicao is null");
        return StreamSupport.stream(posicaoRepository.findAll().spliterator(), false)
            .filter(posicao -> posicao.getEdicao() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Posicao> findOne(Long id) {
        LOG.debug("Request to get Posicao : {}", id);
        return posicaoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Posicao : {}", id);
        posicaoRepository.deleteById(id);
    }
}
