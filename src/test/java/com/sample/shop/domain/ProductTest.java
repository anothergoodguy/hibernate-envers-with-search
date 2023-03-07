package com.sample.shop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(UUID.randomUUID());
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }
}
