package com.microservices.serviceone.domain;

import static com.microservices.serviceone.domain.BrigadeTestSamples.*;
import static com.microservices.serviceone.domain.GovernorateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.serviceone.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GovernorateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Governorate.class);
        Governorate governorate1 = getGovernorateSample1();
        Governorate governorate2 = new Governorate();
        assertThat(governorate1).isNotEqualTo(governorate2);

        governorate2.setId(governorate1.getId());
        assertThat(governorate1).isEqualTo(governorate2);

        governorate2 = getGovernorateSample2();
        assertThat(governorate1).isNotEqualTo(governorate2);
    }

    @Test
    void brigadesTest() {
        Governorate governorate = getGovernorateRandomSampleGenerator();
        Brigade brigadeBack = getBrigadeRandomSampleGenerator();

        governorate.addBrigades(brigadeBack);
        assertThat(governorate.getBrigades()).containsOnly(brigadeBack);
        assertThat(brigadeBack.getGovernorate()).isEqualTo(governorate);

        governorate.removeBrigades(brigadeBack);
        assertThat(governorate.getBrigades()).doesNotContain(brigadeBack);
        assertThat(brigadeBack.getGovernorate()).isNull();

        governorate.brigades(new HashSet<>(Set.of(brigadeBack)));
        assertThat(governorate.getBrigades()).containsOnly(brigadeBack);
        assertThat(brigadeBack.getGovernorate()).isEqualTo(governorate);

        governorate.setBrigades(new HashSet<>());
        assertThat(governorate.getBrigades()).doesNotContain(brigadeBack);
        assertThat(brigadeBack.getGovernorate()).isNull();
    }
}
