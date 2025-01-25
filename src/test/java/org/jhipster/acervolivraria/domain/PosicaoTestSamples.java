package org.jhipster.acervolivraria.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PosicaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Posicao getPosicaoSample1() {
        return new Posicao().id(1L).codigo("codigo1");
    }

    public static Posicao getPosicaoSample2() {
        return new Posicao().id(2L).codigo("codigo2");
    }

    public static Posicao getPosicaoRandomSampleGenerator() {
        return new Posicao().id(longCount.incrementAndGet()).codigo(UUID.randomUUID().toString());
    }
}
