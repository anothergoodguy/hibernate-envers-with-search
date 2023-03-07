package com.sample.shop.service.mapper;

import com.sample.shop.domain.Address;
import com.sample.shop.repository.AddressRepository;
import com.sample.shop.service.dto.AddressDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public abstract class AddressMapper implements EntityMapper<AddressDTO, Address> {

    @Autowired
    AddressRepository mRepository;

    @Mapping(target = "customerId", source = "customer.id")
    public abstract AddressDTO toDto(Address s);

    @Mapping(target = "customer", source = "customerId")
    public abstract Address toEntity(AddressDTO s);

    public Address fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Address entry = mRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Invalid id", getClass().getName(), "Entry id is null"));
        return entry;
    }
}
