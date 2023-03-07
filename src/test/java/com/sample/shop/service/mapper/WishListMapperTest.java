package com.sample.shop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WishListMapperTest {

    private WishListMapper wishListMapper;

    @BeforeEach
    public void setUp() {
        wishListMapper = new WishListMapperImpl();
    }
}
