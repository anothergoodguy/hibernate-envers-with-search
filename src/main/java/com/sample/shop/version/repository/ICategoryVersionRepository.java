package com.sample.shop.version.repository;

import com.sample.shop.version.domain.CategoryVersion;
import java.util.List;
import java.util.UUID;

public interface ICategoryVersionRepository {
    List<CategoryVersion> listCategoryRevisions(UUID categoryId);
}
