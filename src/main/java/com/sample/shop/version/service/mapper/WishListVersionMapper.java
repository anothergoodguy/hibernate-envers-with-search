package com.sample.shop.version.service.mapper;

import com.sample.shop.domain.WishList;
import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link WishList} and its DTO {@link WishListDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerVersionMapper.class })
public interface WishListVersionMapper extends EntityMapper<WishListDTO, WishList> {
    @Mapping(target = "customerId", source = "customer.id")
    WishListDTO toDto(WishList s);
}
