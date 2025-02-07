package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CompraAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompraAllPropertiesEquals(Compra expected, Compra actual) {
        assertCompraAutoGeneratedPropertiesEquals(expected, actual);
        assertCompraAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompraAllUpdatablePropertiesEquals(Compra expected, Compra actual) {
        assertCompraUpdatableFieldsEquals(expected, actual);
        assertCompraUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompraAutoGeneratedPropertiesEquals(Compra expected, Compra actual) {
        assertThat(expected)
            .as("Verify Compra auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompraUpdatableFieldsEquals(Compra expected, Compra actual) {
        assertThat(expected)
            .as("Verify Compra relevant properties")
            .satisfies(e -> assertThat(e.getQuantidade()).as("check quantidade").isEqualTo(actual.getQuantidade()))
            .satisfies(e -> assertThat(e.getPrecoCompra()).as("check precoCompra").isEqualTo(actual.getPrecoCompra()))
            .satisfies(e -> assertThat(e.getValorTotal()).as("check valorTotal").isEqualTo(actual.getValorTotal()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompraUpdatableRelationshipsEquals(Compra expected, Compra actual) {
        assertThat(expected)
            .as("Verify Compra relationships")
            .satisfies(e -> assertThat(e.getEdicao()).as("check edicao").isEqualTo(actual.getEdicao()));
    }
}
