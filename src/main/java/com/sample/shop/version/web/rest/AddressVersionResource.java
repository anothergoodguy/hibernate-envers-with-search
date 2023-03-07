package com.sample.shop.version.web.rest;

import com.sample.shop.service.dto.AddressDTO;
import com.sample.shop.version.domain.AddressVersion;
import com.sample.shop.version.repository.IAddressVersionRepository;
import com.sample.shop.version.service.dto.AddressVersionDTO;
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
@Tag(name = " Version API - Address", description = "Rest API for version information")
public class AddressVersionResource {

    private final IAddressVersionRepository repository;

    @Autowired
    public AddressVersionResource(IAddressVersionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/address/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<AddressVersionDTO> getVersion(@PathVariable("id") UUID id) {
        // Get Version:
        List<AddressVersion> history = repository.listAddressRevisions(id);

        // Return the DTO List:
        return history.stream().map(AddressVersionResource::convert).collect(Collectors.toList());
    }

    public static AddressVersionDTO convert(AddressVersion source) {
        if (source == null) {
            return null;
        }
        AddressDTO target = source.getAddressDTO();
        Long revision = source.getRevision().longValue();
        RevisionTypeDto revisionTypeDto = Converters.convert(source.getRevisionType());
        UUID revisedBy = source.getRevisedBy();
        String revisedByType = source.getRevisedByType();
        String auditor = source.getAuditor();

        return new AddressVersionDTO(target, revision, revisionTypeDto, revisedBy, revisedByType, auditor);
    }
}
