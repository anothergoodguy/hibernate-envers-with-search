package com.sample.shop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WishListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WishListDTO.class);
        WishListDTO wishListDTO1 = new WishListDTO();
        wishListDTO1.setId(UUID.randomUUID());
        WishListDTO wishListDTO2 = new WishListDTO();
        assertThat(wishListDTO1).isNotEqualTo(wishListDTO2);
        wishListDTO2.setId(wishListDTO1.getId());
        assertThat(wishListDTO1).isEqualTo(wishListDTO2);
        wishListDTO2.setId(UUID.randomUUID());
        assertThat(wishListDTO1).isNotEqualTo(wishListDTO2);
        wishListDTO1.setId(null);
        assertThat(wishListDTO1).isNotEqualTo(wishListDTO2);
    }
}
