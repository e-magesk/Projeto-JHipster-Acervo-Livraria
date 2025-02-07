package org.jhipster.acervolivraria.service.impl;

import java.util.Optional;
import org.jhipster.acervolivraria.domain.Autor;
import org.jhipster.acervolivraria.domain.Livro;
import org.jhipster.acervolivraria.repository.LivroRepository;
import org.jhipster.acervolivraria.service.AutorService;
import org.jhipster.acervolivraria.service.LivroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster.acervolivraria.domain.Livro}.
 */
@Service
@Transactional
public class LivroServiceImpl implements LivroService {

    private static final Logger LOG = LoggerFactory.getLogger(LivroServiceImpl.class);

    private final LivroRepository livroRepository;

    @Autowired
    private AutorService autorService;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public Livro save(Livro livro) {
        LOG.debug("Request to save Livro : {}", livro);
        Livro aux = livroRepository.save(livro);
        for (Autor autor : aux.getAutors()) {
            livro.addAutor(autor);
        }
        for (Autor autor : livro.getAutors()) {
            autor.addLivro(livro);
            this.autorService.update(autor);
        }

        return livroRepository.save(livro);
    }

    @Override
    public Livro update(Livro livro) {
        LOG.debug("Request to update Livro : {}", livro);
        Livro aux = livroRepository.getOne(livro.getId());
        for (Autor autor : aux.getAutors()) {
            livro.addAutor(autor);
        }
        for (Autor autor : livro.getAutors()) {
            autor.addLivro(livro);
            this.autorService.update(autor);
        }
        return livroRepository.save(livro);
    }

    @Override
    public Optional<Livro> partialUpdate(Livro livro) {
        LOG.debug("Request to partially update Livro : {}", livro);

        return livroRepository
            .findById(livro.getId())
            .map(existingLivro -> {
                if (livro.getTitulo() != null) {
                    existingLivro.setTitulo(livro.getTitulo());
                }
                if (livro.getGenero() != null) {
                    existingLivro.setGenero(livro.getGenero());
                }

                return existingLivro;
            })
            .map(livroRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livro> findAll(Pageable pageable) {
        LOG.debug("Request to get all Livros");
        return livroRepository.findAll(pageable);
    }

    public Page<Livro> findAllWithEagerRelationships(Pageable pageable) {
        return livroRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livro> findOne(Long id) {
        LOG.debug("Request to get Livro : {}", id);
        return livroRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Livro : {}", id);
        Livro livro = livroRepository.findById(id).get();
        for (Autor autor : livro.getAutors()) {
            autor.getLivros().remove(livro);
            this.autorService.update(autor);
        }
        livroRepository.deleteById(id);
    }
}
