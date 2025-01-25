package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster.acervolivraria.domain.CompraTestSamples.*;
import static org.jhipster.acervolivraria.domain.EdicaoTestSamples.*;

import org.jhipster.acervolivraria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compra.class);
        Compra compra1 = getCompraSample1();
        Compra compra2 = new Compra();
        assertThat(compra1).isNotEqualTo(compra2);

        compra2.setId(compra1.getId());
        assertThat(compra1).isEqualTo(compra2);

        compra2 = getCompraSample2();
        assertThat(compra1).isNotEqualTo(compra2);
    }

    @Test
    void edicaoTest() {
        Compra compra = getCompraRandomSampleGenerator();
        Edicao edicaoBack = getEdicaoRandomSampleGenerator();

        compra.setEdicao(edicaoBack);
        assertThat(compra.getEdicao()).isEqualTo(edicaoBack);

        compra.edicao(null);
        assertThat(compra.getEdicao()).isNull();
    }
}
