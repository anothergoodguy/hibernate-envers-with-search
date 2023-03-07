package com.sample.shop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.shop.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WishListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WishList.class);
        WishList wishList1 = new WishList();
        wishList1.setId(UUID.randomUUID());
        WishList wishList2 = new WishList();
        wishList2.setId(wishList1.getId());
        assertThat(wishList1).isEqualTo(wishList2);
        wishList2.setId(UUID.randomUUID());
        assertThat(wishList1).isNotEqualTo(wishList2);
        wishList1.setId(null);
        assertThat(wishList1).isNotEqualTo(wishList2);
    }
}
