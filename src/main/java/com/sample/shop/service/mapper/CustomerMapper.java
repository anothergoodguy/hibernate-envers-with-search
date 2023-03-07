package com.sample.shop.service.mapper;

import com.sample.shop.domain.Customer;
import com.sample.shop.repository.CustomerRepository;
import com.sample.shop.service.dto.CustomerDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public abstract class CustomerMapper implements EntityMapper<CustomerDTO, Customer> {

    @Autowired
    CustomerRepository mRepository;

    public abstract CustomerDTO toDto(Customer s);

    public abstract Customer toEntity(CustomerDTO s);

    public Customer fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Customer entry = mRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Invalid id", getClass().getName(), "Entry id is null"));
        return entry;
    }
}
