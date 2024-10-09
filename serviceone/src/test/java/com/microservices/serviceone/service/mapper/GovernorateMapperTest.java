package com.microservices.serviceone.service.mapper;

import static com.microservices.serviceone.domain.GovernorateAsserts.*;
import static com.microservices.serviceone.domain.GovernorateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GovernorateMapperTest {

    private GovernorateMapper governorateMapper;

    @BeforeEach
    void setUp() {
        governorateMapper = new GovernorateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGovernorateSample1();
        var actual = governorateMapper.toEntity(governorateMapper.toDto(expected));
        assertGovernorateAllPropertiesEquals(expected, actual);
    }
}
