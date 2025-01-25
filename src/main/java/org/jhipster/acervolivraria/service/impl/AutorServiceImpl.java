package org.jhipster.acervolivraria.service.impl;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Autor;
import org.jhipster.acervolivraria.repository.AutorRepository;
import org.jhipster.acervolivraria.service.AutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster.acervolivraria.domain.Autor}.
 */
@Service
@Transactional
public class AutorServiceImpl implements AutorService {

    private static final Logger LOG = LoggerFactory.getLogger(AutorServiceImpl.class);

    private final AutorRepository autorRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public Autor save(Autor autor) {
        LOG.debug("Request to save Autor : {}", autor);
        return autorRepository.save(autor);
    }

    @Override
    public Autor update(Autor autor) {
        LOG.debug("Request to update Autor : {}", autor);
        return autorRepository.save(autor);
    }

    @Override
    public Optional<Autor> partialUpdate(Autor autor) {
        LOG.debug("Request to partially update Autor : {}", autor);

        return autorRepository
            .findById(autor.getId())
            .map(existingAutor -> {
                if (autor.getNome() != null) {
                    existingAutor.setNome(autor.getNome());
                }
                if (autor.getNacionalidade() != null) {
                    existingAutor.setNacionalidade(autor.getNacionalidade());
                }

                return existingAutor;
            })
            .map(autorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Autor> findAll(Pageable pageable) {
        LOG.debug("Request to get all Autors");
        return autorRepository.findAll(pageable);
    }

    public Page<Autor> findAllWithEagerRelationships(Pageable pageable) {
        return autorRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Autor> findOne(Long id) {
        LOG.debug("Request to get Autor : {}", id);
        return autorRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Autor : {}", id);
        autorRepository.deleteById(id);
    }
}
