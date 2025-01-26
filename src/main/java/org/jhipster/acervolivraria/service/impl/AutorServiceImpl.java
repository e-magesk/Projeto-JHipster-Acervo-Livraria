package org.jhipster.acervolivraria.service.impl;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Autor;
import org.jhipster.acervolivraria.domain.Livro;
import org.jhipster.acervolivraria.repository.AutorRepository;
import org.jhipster.acervolivraria.repository.LivroRepository;
import org.jhipster.acervolivraria.service.AutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LivroRepository livroRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public Autor save(Autor autor) {
        LOG.debug("Request to save Autor : {}", autor);
        Autor aux = autorRepository.save(autor);
        for (Livro livro : aux.getLivros()) {
            autor.addLivro(livro);
        }
        for (Livro livro : autor.getLivros()) {
            livro.addAutor(autor);
            livroRepository.save(livro);
        }
        return autorRepository.save(autor);
    }

    @Override
    public Autor update(Autor autor) {
        LOG.debug("Request to update Autor : {}", autor);
        Autor aux = autorRepository.getOne(autor.getId());
        for (Livro livro : aux.getLivros()) {
            autor.addLivro(livro);
        }
        for (Livro livro : autor.getLivros()) {
            livro.addAutor(autor);
            livroRepository.save(livro);
        }
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

    @Override
    @Transactional(readOnly = true)
    public Optional<Autor> findOne(Long id) {
        LOG.debug("Request to get Autor : {}", id);
        return autorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Autor : {}", id);
        Autor autor = autorRepository.findById(id).get();
        for (Livro livro : autor.getLivros()) {
            livro.getAutors().remove(autor);
            livroRepository.save(livro);
        }
        autorRepository.deleteById(id);
    }
}
