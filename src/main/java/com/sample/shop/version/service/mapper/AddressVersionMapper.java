package com.sample.shop.version.service.mapper;

import com.sample.shop.domain.Address;
import com.sample.shop.service.dto.AddressDTO;
import com.sample.shop.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerVersionMapper.class })
public interface AddressVersionMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "customerId", source = "customer.id")
    AddressDTO toDto(Address s);
}
