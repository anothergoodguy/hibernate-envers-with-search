package com.sample.shop.version.repository;

import com.sample.shop.version.domain.ProductVersion;
import java.util.List;
import java.util.UUID;

public interface IProductVersionRepository {
    List<ProductVersion> listProductRevisions(UUID productId);
}
