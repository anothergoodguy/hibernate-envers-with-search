package com.sample.shop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = new Category();
        category1.setId(UUID.randomUUID());
        Category category2 = new Category();
        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);
        category2.setId(UUID.randomUUID());
        assertThat(category1).isNotEqualTo(category2);
        category1.setId(null);
        assertThat(category1).isNotEqualTo(category2);
    }
}
