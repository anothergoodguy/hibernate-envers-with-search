package com.sample.shop.version.repository;

import com.sample.shop.version.domain.WishListVersion;
import java.util.List;
import java.util.UUID;

public interface IWishListVersionRepository {
    List<WishListVersion> listWishListRevisions(UUID wishListId);
}
