package com.microservices.serviceone.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MunicipalityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMunicipalityAllPropertiesEquals(Municipality expected, Municipality actual) {
        assertMunicipalityAutoGeneratedPropertiesEquals(expected, actual);
        assertMunicipalityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMunicipalityAllUpdatablePropertiesEquals(Municipality expected, Municipality actual) {
        assertMunicipalityUpdatableFieldsEquals(expected, actual);
        assertMunicipalityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMunicipalityAutoGeneratedPropertiesEquals(Municipality expected, Municipality actual) {
        assertThat(expected)
            .as("Verify Municipality auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMunicipalityUpdatableFieldsEquals(Municipality expected, Municipality actual) {
        assertThat(expected)
            .as("Verify Municipality relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMunicipalityUpdatableRelationshipsEquals(Municipality expected, Municipality actual) {
        assertThat(expected)
            .as("Verify Municipality relationships")
            .satisfies(e -> assertThat(e.getBrigade()).as("check brigade").isEqualTo(actual.getBrigade()));
    }
}
