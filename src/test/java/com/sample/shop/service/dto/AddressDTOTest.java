package com.sample.shop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDTO.class);
        AddressDTO addressDTO1 = new AddressDTO();
        addressDTO1.setId(UUID.randomUUID());
        AddressDTO addressDTO2 = new AddressDTO();
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
        addressDTO2.setId(addressDTO1.getId());
        assertThat(addressDTO1).isEqualTo(addressDTO2);
        addressDTO2.setId(UUID.randomUUID());
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
        addressDTO1.setId(null);
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
    }
}
