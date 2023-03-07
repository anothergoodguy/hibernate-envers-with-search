package com.sample.shop.version.web.rest;

import com.sample.shop.service.dto.CategoryDTO;
import com.sample.shop.version.domain.CategoryVersion;
import com.sample.shop.version.repository.ICategoryVersionRepository;
import com.sample.shop.version.service.dto.CategoryVersionDTO;
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
@Tag(name = " Version API - Category", description = "Rest API for version information")
public class CategoryVersionResource {

    private final ICategoryVersionRepository repository;

    @Autowired
    public CategoryVersionResource(ICategoryVersionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<CategoryVersionDTO> getVersion(@PathVariable("id") UUID id) {
        // Get Version:
        List<CategoryVersion> history = repository.listCategoryRevisions(id);

        // Return the DTO List:
        return history.stream().map(CategoryVersionResource::convert).collect(Collectors.toList());
    }

    public static CategoryVersionDTO convert(CategoryVersion source) {
        if (source == null) {
            return null;
        }
        CategoryDTO target = source.getCategoryDTO();
        Long revision = source.getRevision().longValue();
        RevisionTypeDto revisionTypeDto = Converters.convert(source.getRevisionType());
        UUID revisedBy = source.getRevisedBy();
        String revisedByType = source.getRevisedByType();
        String auditor = source.getAuditor();

        return new CategoryVersionDTO(target, revision, revisionTypeDto, revisedBy, revisedByType, auditor);
    }
}
