package com.microservices.serviceone.domain;

import static com.microservices.serviceone.domain.BrigadeTestSamples.*;
import static com.microservices.serviceone.domain.MunicipalityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.serviceone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MunicipalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Municipality.class);
        Municipality municipality1 = getMunicipalitySample1();
        Municipality municipality2 = new Municipality();
        assertThat(municipality1).isNotEqualTo(municipality2);

        municipality2.setId(municipality1.getId());
        assertThat(municipality1).isEqualTo(municipality2);

        municipality2 = getMunicipalitySample2();
        assertThat(municipality1).isNotEqualTo(municipality2);
    }

    @Test
    void brigadesTest() {
        Municipality municipality = getMunicipalityRandomSampleGenerator();
        Brigade brigadeBack = getBrigadeRandomSampleGenerator();

        municipality.setBrigades(brigadeBack);
        assertThat(municipality.getBrigades()).isEqualTo(brigadeBack);

        municipality.brigades(null);
        assertThat(municipality.getBrigades()).isNull();
    }
}
