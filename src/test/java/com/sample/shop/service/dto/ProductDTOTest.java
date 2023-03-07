package com.sample.shop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(UUID.randomUUID());
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(UUID.randomUUID());
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }
}
