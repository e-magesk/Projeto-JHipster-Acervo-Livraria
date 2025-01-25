package org.jhipster.acervolivraria.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Compra getCompraSample1() {
        return new Compra().id(1L).quantidade(1);
    }

    public static Compra getCompraSample2() {
        return new Compra().id(2L).quantidade(2);
    }

    public static Compra getCompraRandomSampleGenerator() {
        return new Compra().id(longCount.incrementAndGet()).quantidade(intCount.incrementAndGet());
    }
}
