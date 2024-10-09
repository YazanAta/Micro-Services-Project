package com.microservices.serviceone.service.mapper;

import static com.microservices.serviceone.domain.BrigadeAsserts.*;
import static com.microservices.serviceone.domain.BrigadeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BrigadeMapperTest {

    private BrigadeMapper brigadeMapper;

    @BeforeEach
    void setUp() {
        brigadeMapper = new BrigadeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBrigadeSample1();
        var actual = brigadeMapper.toEntity(brigadeMapper.toDto(expected));
        assertBrigadeAllPropertiesEquals(expected, actual);
    }
}
