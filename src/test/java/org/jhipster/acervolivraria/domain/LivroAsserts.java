package org.jhipster.acervolivraria.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class LivroAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLivroAllPropertiesEquals(Livro expected, Livro actual) {
        assertLivroAutoGeneratedPropertiesEquals(expected, actual);
        assertLivroAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLivroAllUpdatablePropertiesEquals(Livro expected, Livro actual) {
        assertLivroUpdatableFieldsEquals(expected, actual);
        assertLivroUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLivroAutoGeneratedPropertiesEquals(Livro expected, Livro actual) {
        assertThat(expected)
            .as("Verify Livro auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLivroUpdatableFieldsEquals(Livro expected, Livro actual) {
        assertThat(expected)
            .as("Verify Livro relevant properties")
            .satisfies(e -> assertThat(e.getTitulo()).as("check titulo").isEqualTo(actual.getTitulo()))
            .satisfies(e -> assertThat(e.getGenero()).as("check genero").isEqualTo(actual.getGenero()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLivroUpdatableRelationshipsEquals(Livro expected, Livro actual) {
        assertThat(expected)
            .as("Verify Livro relationships")
            .satisfies(e -> assertThat(e.getAutors()).as("check autors").isEqualTo(actual.getAutors()));
    }
}
