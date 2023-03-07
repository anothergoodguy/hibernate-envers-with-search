package com.sample.shop.version.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.shop.service.dto.CategoryDTO;
import java.util.UUID;

public class CategoryVersionDTO {

    private final CategoryDTO category;
    private final Long revision;
    private final RevisionTypeDto type;
    private final UUID revisedBy;
    private final String revisedByType;
    private final String auditor;

    public CategoryVersionDTO(
        CategoryDTO category,
        Long revision,
        RevisionTypeDto type,
        UUID revisedBy,
        String revisedByType,
        String auditor
    ) {
        this.category = category;
        this.revision = revision;
        this.type = type;
        this.revisedBy = revisedBy;
        this.revisedByType = revisedByType;
        this.auditor = auditor;
    }

    @JsonProperty("category")
    public CategoryDTO getCategory() {
        return category;
    }

    @JsonProperty("revision")
    public Long getRevision() {
        return revision;
    }

    @JsonProperty("type")
    public RevisionTypeDto getType() {
        return type;
    }

    @JsonProperty("revisedBy")
    public UUID getRevisedBy() {
        return revisedBy;
    }

    @JsonProperty("revisedByType")
    public String getRevisedByType() {
        return revisedByType;
    }

    @JsonProperty("auditor")
    public String getAuditor() {
        return auditor;
    }
}
