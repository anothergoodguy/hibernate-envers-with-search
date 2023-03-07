package com.sample.shop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = new Address();
        address1.setId(UUID.randomUUID());
        Address address2 = new Address();
        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);
        address2.setId(UUID.randomUUID());
        assertThat(address1).isNotEqualTo(address2);
        address1.setId(null);
        assertThat(address1).isNotEqualTo(address2);
    }
}
