package com.sample.shop.version.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.shop.service.dto.WishListDTO;
import java.util.UUID;

public class WishListVersionDTO {

    private final WishListDTO wishList;
    private final Long revision;
    private final RevisionTypeDto type;
    private final UUID revisedBy;
    private final String revisedByType;
    private final String auditor;

    public WishListVersionDTO(
        WishListDTO wishList,
        Long revision,
        RevisionTypeDto type,
        UUID revisedBy,
        String revisedByType,
        String auditor
    ) {
        this.wishList = wishList;
        this.revision = revision;
        this.type = type;
        this.revisedBy = revisedBy;
        this.revisedByType = revisedByType;
        this.auditor = auditor;
    }

    @JsonProperty("wishList")
    public WishListDTO getWishList() {
        return wishList;
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
