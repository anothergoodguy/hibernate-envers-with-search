package com.sample.shop.version.web.rest;

import com.sample.shop.service.dto.CustomerDTO;
import com.sample.shop.version.domain.CustomerVersion;
import com.sample.shop.version.repository.ICustomerVersionRepository;
import com.sample.shop.version.service.dto.CustomerVersionDTO;
import com.sample.shop.version.service.dto.RevisionTypeDto;
import com.sample.shop.version.service.mapper.Converters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/api/history")
@Tag(name = " Version API - Customer", description = "Rest API for version information")
public class CustomerVersionResource {

    private final ICustomerVersionRepository repository;

    @Autowired
    public CustomerVersionResource(ICustomerVersionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<CustomerVersionDTO> getVersion(@PathVariable("id") UUID id) {
        // Get Version:
        List<CustomerVersion> history = repository.listCustomerRevisions(id);

        // Return the DTO List:
        return history.stream().map(CustomerVersionResource::convert).collect(Collectors.toList());
    }

    public static CustomerVersionDTO convert(CustomerVersion source) {
        if (source == null) {
            return null;
        }
        CustomerDTO target = source.getCustomerDTO();
        Long revision = source.getRevision().longValue();
        RevisionTypeDto revisionTypeDto = Converters.convert(source.getRevisionType());
        UUID revisedBy = source.getRevisedBy();
        String revisedByType = source.getRevisedByType();
        String auditor = source.getAuditor();

        return new CustomerVersionDTO(target, revision, revisionTypeDto, revisedBy, revisedByType, auditor);
    }
}
