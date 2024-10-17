package com.microservices.serviceone.domain;

import static com.microservices.serviceone.domain.BrigadeTestSamples.*;
import static com.microservices.serviceone.domain.GovernorateTestSamples.*;
import static com.microservices.serviceone.domain.MunicipalityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.serviceone.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BrigadeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Brigade.class);
        Brigade brigade1 = getBrigadeSample1();
        Brigade brigade2 = new Brigade();
        assertThat(brigade1).isNotEqualTo(brigade2);

        brigade2.setId(brigade1.getId());
        assertThat(brigade1).isEqualTo(brigade2);

        brigade2 = getBrigadeSample2();
        assertThat(brigade1).isNotEqualTo(brigade2);
    }

    @Test
    void municipalityTest() {
        Brigade brigade = getBrigadeRandomSampleGenerator();
        Municipality municipalityBack = getMunicipalityRandomSampleGenerator();

        brigade.addMunicipality(municipalityBack);
        assertThat(brigade.getMunicipalities()).containsOnly(municipalityBack);
        assertThat(municipalityBack.getBrigade()).isEqualTo(brigade);

        brigade.removeMunicipality(municipalityBack);
        assertThat(brigade.getMunicipalities()).doesNotContain(municipalityBack);
        assertThat(municipalityBack.getBrigade()).isNull();

        brigade.municipalities(new HashSet<>(Set.of(municipalityBack)));
        assertThat(brigade.getMunicipalities()).containsOnly(municipalityBack);
        assertThat(municipalityBack.getBrigade()).isEqualTo(brigade);

        brigade.setMunicipalities(new HashSet<>());
        assertThat(brigade.getMunicipalities()).doesNotContain(municipalityBack);
        assertThat(municipalityBack.getBrigade()).isNull();
    }

    @Test
    void governorateTest() {
        Brigade brigade = getBrigadeRandomSampleGenerator();
        Governorate governorateBack = getGovernorateRandomSampleGenerator();

        brigade.setGovernorate(governorateBack);
        assertThat(brigade.getGovernorate()).isEqualTo(governorateBack);

        brigade.governorate(null);
        assertThat(brigade.getGovernorate()).isNull();
    }
}
