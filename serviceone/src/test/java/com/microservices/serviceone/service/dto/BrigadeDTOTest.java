package com.microservices.serviceone.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.serviceone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BrigadeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrigadeDTO.class);
        BrigadeDTO brigadeDTO1 = new BrigadeDTO();
        brigadeDTO1.setId(1L);
        BrigadeDTO brigadeDTO2 = new BrigadeDTO();
        assertThat(brigadeDTO1).isNotEqualTo(brigadeDTO2);
        brigadeDTO2.setId(brigadeDTO1.getId());
        assertThat(brigadeDTO1).isEqualTo(brigadeDTO2);
        brigadeDTO2.setId(2L);
        assertThat(brigadeDTO1).isNotEqualTo(brigadeDTO2);
        brigadeDTO1.setId(null);
        assertThat(brigadeDTO1).isNotEqualTo(brigadeDTO2);
    }
}
