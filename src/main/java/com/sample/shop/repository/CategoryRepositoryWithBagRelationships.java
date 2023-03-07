package com.sample.shop.repository;

import com.sample.shop.domain.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface CategoryRepositoryWithBagRelationships {
    Optional<Category> fetchBagRelationships(Optional<Category> category);

    List<Category> fetchBagRelationships(List<Category> categories);

    Page<Category> fetchBagRelationships(Page<Category> categories);
}
