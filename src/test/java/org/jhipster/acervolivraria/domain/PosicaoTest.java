package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster.acervolivraria.domain.EdicaoTestSamples.*;
import static org.jhipster.acervolivraria.domain.PosicaoTestSamples.*;

import org.jhipster.acervolivraria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PosicaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posicao.class);
        Posicao posicao1 = getPosicaoSample1();
        Posicao posicao2 = new Posicao();
        assertThat(posicao1).isNotEqualTo(posicao2);

        posicao2.setId(posicao1.getId());
        assertThat(posicao1).isEqualTo(posicao2);

        posicao2 = getPosicaoSample2();
        assertThat(posicao1).isNotEqualTo(posicao2);
    }

    @Test
    void edicaoTest() {
        Posicao posicao = getPosicaoRandomSampleGenerator();
        Edicao edicaoBack = getEdicaoRandomSampleGenerator();

        posicao.setEdicao(edicaoBack);
        assertThat(posicao.getEdicao()).isEqualTo(edicaoBack);
        assertThat(edicaoBack.getPosicao()).isEqualTo(posicao);

        posicao.edicao(null);
        assertThat(posicao.getEdicao()).isNull();
        assertThat(edicaoBack.getPosicao()).isNull();
    }
}
