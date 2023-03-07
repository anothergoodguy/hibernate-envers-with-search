package com.sample.shop.service.mapper;

import com.sample.shop.domain.WishList;
import com.sample.shop.repository.WishListRepository;
import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link WishList} and its DTO {@link WishListDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public abstract class WishListMapper implements EntityMapper<WishListDTO, WishList> {

    @Autowired
    WishListRepository mRepository;

    @Mapping(target = "customerId", source = "customer.id")
    public abstract WishListDTO toDto(WishList s);

    @Mapping(target = "customer", source = "customerId")
    public abstract WishList toEntity(WishListDTO s);

    public WishList fromId(UUID id) {
        if (id == null) {
            return null;
        }
        WishList entry = mRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Invalid id", getClass().getName(), "Entry id is null"));
        return entry;
    }
}
