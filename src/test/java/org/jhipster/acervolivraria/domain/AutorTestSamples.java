package org.jhipster.acervolivraria.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AutorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Autor getAutorSample1() {
        return new Autor().id(1L).nome("nome1");
    }

    public static Autor getAutorSample2() {
        return new Autor().id(2L).nome("nome2");
    }

    public static Autor getAutorRandomSampleGenerator() {
        return new Autor().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
