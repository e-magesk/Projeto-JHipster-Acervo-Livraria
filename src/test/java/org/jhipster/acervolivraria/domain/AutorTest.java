package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster.acervolivraria.domain.AutorTestSamples.*;
import static org.jhipster.acervolivraria.domain.LivroTestSamples.*;

import org.jhipster.acervolivraria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autor.class);
        Autor autor1 = getAutorSample1();
        Autor autor2 = new Autor();
        assertThat(autor1).isNotEqualTo(autor2);

        autor2.setId(autor1.getId());
        assertThat(autor1).isEqualTo(autor2);

        autor2 = getAutorSample2();
        assertThat(autor1).isNotEqualTo(autor2);
    }

    @Test
    void livroTest() {
        Autor autor = getAutorRandomSampleGenerator();
        Livro livroBack = getLivroRandomSampleGenerator();

        autor.setLivro(livroBack);
        assertThat(autor.getLivro()).isEqualTo(livroBack);

        autor.livro(null);
        assertThat(autor.getLivro()).isNull();
    }
}
