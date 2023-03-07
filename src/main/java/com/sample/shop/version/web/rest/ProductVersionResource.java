package com.sample.shop.version.web.rest;

import com.sample.shop.service.dto.ProductDTO;
import com.sample.shop.version.domain.ProductVersion;
import com.sample.shop.version.repository.IProductVersionRepository;
import com.sample.shop.version.service.dto.ProductVersionDTO;
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
@Tag(name = " Version API - Product", description = "Rest API for version information")
public class ProductVersionResource {

    private final IProductVersionRepository repository;

    @Autowired
    public ProductVersionResource(IProductVersionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<ProductVersionDTO> getVersion(@PathVariable("id") UUID id) {
        // Get Version:
        List<ProductVersion> history = repository.listProductRevisions(id);

        // Return the DTO List:
        return history.stream().map(ProductVersionResource::convert).collect(Collectors.toList());
    }

    public static ProductVersionDTO convert(ProductVersion source) {
        if (source == null) {
            return null;
        }
        ProductDTO target = source.getProductDTO();
        Long revision = source.getRevision().longValue();
        RevisionTypeDto revisionTypeDto = Converters.convert(source.getRevisionType());
        UUID revisedBy = source.getRevisedBy();
        String revisedByType = source.getRevisedByType();
        String auditor = source.getAuditor();

        return new ProductVersionDTO(target, revision, revisionTypeDto, revisedBy, revisedByType, auditor);
    }
}
