package com.sample.shop.repository;

import com.sample.shop.domain.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Category entity.
 *
 * When extending this class, extend CategoryRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CategoryRepository extends CategoryRepositoryWithBagRelationships, JpaRepository<Category, UUID> {
    default Optional<Category> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Category> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Category> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
