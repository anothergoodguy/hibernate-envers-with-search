package com.sample.shop.repository;

import com.sample.shop.domain.WishList;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WishList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishListRepository extends JpaRepository<WishList, UUID> {}
