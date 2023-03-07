package com.sample.shop.service.mapper;

import com.sample.shop.domain.Product;
import com.sample.shop.repository.ProductRepository;
import com.sample.shop.service.dto.ProductDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { WishListMapper.class })
public abstract class ProductMapper implements EntityMapper<ProductDTO, Product> {

    @Autowired
    ProductRepository mRepository;

    @Mapping(target = "wishListId", source = "wishList.id")
    public abstract ProductDTO toDto(Product s);

    @Mapping(target = "wishList", source = "wishListId")
    public abstract Product toEntity(ProductDTO s);

    public Product fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Product entry = mRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Invalid id", getClass().getName(), "Entry id is null"));
        return entry;
    }
}
