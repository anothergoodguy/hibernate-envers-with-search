package com.sample.shop.version.service.mapper;

import com.sample.shop.domain.Product;
import com.sample.shop.service.dto.ProductDTO;
import com.sample.shop.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { WishListVersionMapper.class })
public interface ProductVersionMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "wishListId", source = "wishList.id")
    ProductDTO toDto(Product s);
}
