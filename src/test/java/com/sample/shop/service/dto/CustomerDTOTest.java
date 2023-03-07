package com.sample.shop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerDTO.class);
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId(UUID.randomUUID());
        CustomerDTO customerDTO2 = new CustomerDTO();
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO2.setId(customerDTO1.getId());
        assertThat(customerDTO1).isEqualTo(customerDTO2);
        customerDTO2.setId(UUID.randomUUID());
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO1.setId(null);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
    }
}
