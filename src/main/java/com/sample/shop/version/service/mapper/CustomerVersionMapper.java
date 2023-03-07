package com.sample.shop.version.service.mapper;

import com.sample.shop.domain.Customer;
import com.sample.shop.service.dto.CustomerDTO;
import com.sample.shop.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerVersionMapper extends EntityMapper<CustomerDTO, Customer> {
    CustomerDTO toDto(Customer s);
}
