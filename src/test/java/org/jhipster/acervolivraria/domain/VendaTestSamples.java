package org.jhipster.acervolivraria.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VendaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Venda getVendaSample1() {
        return new Venda().id(1L).quantidade(1);
    }

    public static Venda getVendaSample2() {
        return new Venda().id(2L).quantidade(2);
    }

    public static Venda getVendaRandomSampleGenerator() {
        return new Venda().id(longCount.incrementAndGet()).quantidade(intCount.incrementAndGet());
    }
}
