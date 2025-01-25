package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster.acervolivraria.domain.EdicaoTestSamples.*;
import static org.jhipster.acervolivraria.domain.LivroTestSamples.*;
import static org.jhipster.acervolivraria.domain.PosicaoTestSamples.*;

import org.jhipster.acervolivraria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EdicaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Edicao.class);
        Edicao edicao1 = getEdicaoSample1();
        Edicao edicao2 = new Edicao();
        assertThat(edicao1).isNotEqualTo(edicao2);

        edicao2.setId(edicao1.getId());
        assertThat(edicao1).isEqualTo(edicao2);

        edicao2 = getEdicaoSample2();
        assertThat(edicao1).isNotEqualTo(edicao2);
    }

    @Test
    void posicaoTest() {
        Edicao edicao = getEdicaoRandomSampleGenerator();
        Posicao posicaoBack = getPosicaoRandomSampleGenerator();

        edicao.setPosicao(posicaoBack);
        assertThat(edicao.getPosicao()).isEqualTo(posicaoBack);

        edicao.posicao(null);
        assertThat(edicao.getPosicao()).isNull();
    }

    @Test
    void livroTest() {
        Edicao edicao = getEdicaoRandomSampleGenerator();
        Livro livroBack = getLivroRandomSampleGenerator();

        edicao.setLivro(livroBack);
        assertThat(edicao.getLivro()).isEqualTo(livroBack);

        edicao.livro(null);
        assertThat(edicao.getLivro()).isNull();
    }
}
