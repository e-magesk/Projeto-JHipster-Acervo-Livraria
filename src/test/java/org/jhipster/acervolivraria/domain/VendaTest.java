package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster.acervolivraria.domain.EdicaoTestSamples.*;
import static org.jhipster.acervolivraria.domain.VendaTestSamples.*;

import org.jhipster.acervolivraria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VendaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venda.class);
        Venda venda1 = getVendaSample1();
        Venda venda2 = new Venda();
        assertThat(venda1).isNotEqualTo(venda2);

        venda2.setId(venda1.getId());
        assertThat(venda1).isEqualTo(venda2);

        venda2 = getVendaSample2();
        assertThat(venda1).isNotEqualTo(venda2);
    }

    @Test
    void edicaoTest() {
        Venda venda = getVendaRandomSampleGenerator();
        Edicao edicaoBack = getEdicaoRandomSampleGenerator();

        venda.setEdicao(edicaoBack);
        assertThat(venda.getEdicao()).isEqualTo(edicaoBack);

        venda.edicao(null);
        assertThat(venda.getEdicao()).isNull();
    }
}
