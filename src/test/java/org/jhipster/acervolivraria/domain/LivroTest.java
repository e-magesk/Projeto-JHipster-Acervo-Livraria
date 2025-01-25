package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster.acervolivraria.domain.AutorTestSamples.*;
import static org.jhipster.acervolivraria.domain.EdicaoTestSamples.*;
import static org.jhipster.acervolivraria.domain.LivroTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.jhipster.acervolivraria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livro.class);
        Livro livro1 = getLivroSample1();
        Livro livro2 = new Livro();
        assertThat(livro1).isNotEqualTo(livro2);

        livro2.setId(livro1.getId());
        assertThat(livro1).isEqualTo(livro2);

        livro2 = getLivroSample2();
        assertThat(livro1).isNotEqualTo(livro2);
    }

    @Test
    void edicaoTest() {
        Livro livro = getLivroRandomSampleGenerator();
        Edicao edicaoBack = getEdicaoRandomSampleGenerator();

        livro.addEdicao(edicaoBack);
        assertThat(livro.getEdicaos()).containsOnly(edicaoBack);
        assertThat(edicaoBack.getLivro()).isEqualTo(livro);

        livro.removeEdicao(edicaoBack);
        assertThat(livro.getEdicaos()).doesNotContain(edicaoBack);
        assertThat(edicaoBack.getLivro()).isNull();

        livro.edicaos(new HashSet<>(Set.of(edicaoBack)));
        assertThat(livro.getEdicaos()).containsOnly(edicaoBack);
        assertThat(edicaoBack.getLivro()).isEqualTo(livro);

        livro.setEdicaos(new HashSet<>());
        assertThat(livro.getEdicaos()).doesNotContain(edicaoBack);
        assertThat(edicaoBack.getLivro()).isNull();
    }

    @Test
    void autorTest() {
        Livro livro = getLivroRandomSampleGenerator();
        Autor autorBack = getAutorRandomSampleGenerator();

        livro.addAutor(autorBack);
        assertThat(livro.getAutors()).containsOnly(autorBack);
        assertThat(autorBack.getLivro()).isEqualTo(livro);

        livro.removeAutor(autorBack);
        assertThat(livro.getAutors()).doesNotContain(autorBack);
        assertThat(autorBack.getLivro()).isNull();

        livro.autors(new HashSet<>(Set.of(autorBack)));
        assertThat(livro.getAutors()).containsOnly(autorBack);
        assertThat(autorBack.getLivro()).isEqualTo(livro);

        livro.setAutors(new HashSet<>());
        assertThat(livro.getAutors()).doesNotContain(autorBack);
        assertThat(autorBack.getLivro()).isNull();
    }
}
