package com.microservices.serviceone.service.mapper;

import static com.microservices.serviceone.domain.MunicipalityAsserts.*;
import static com.microservices.serviceone.domain.MunicipalityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MunicipalityMapperTest {

    private MunicipalityMapper municipalityMapper;

    @BeforeEach
    void setUp() {
        municipalityMapper = new MunicipalityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMunicipalitySample1();
        var actual = municipalityMapper.toEntity(municipalityMapper.toDto(expected));
        assertMunicipalityAllPropertiesEquals(expected, actual);
    }
}
