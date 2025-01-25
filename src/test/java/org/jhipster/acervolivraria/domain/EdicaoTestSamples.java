package org.jhipster.acervolivraria.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EdicaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Edicao getEdicaoSample1() {
        return new Edicao().id(1L).editora("editora1").quantidadeExemplares(1);
    }

    public static Edicao getEdicaoSample2() {
        return new Edicao().id(2L).editora("editora2").quantidadeExemplares(2);
    }

    public static Edicao getEdicaoRandomSampleGenerator() {
        return new Edicao()
            .id(longCount.incrementAndGet())
            .editora(UUID.randomUUID().toString())
            .quantidadeExemplares(intCount.incrementAndGet());
    }
}
