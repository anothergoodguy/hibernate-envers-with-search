package com.sample.shop.version.web.rest;

import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.version.domain.WishListVersion;
import com.sample.shop.version.repository.IWishListVersionRepository;
import com.sample.shop.version.service.dto.RevisionTypeDto;
import com.sample.shop.version.service.dto.WishListVersionDTO;
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
@Tag(name = " Version API - WishList", description = "Rest API for version information")
public class WishListVersionResource {

    private final IWishListVersionRepository repository;

    @Autowired
    public WishListVersionResource(IWishListVersionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/wishList/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<WishListVersionDTO> getVersion(@PathVariable("id") UUID id) {
        // Get Version:
        List<WishListVersion> history = repository.listWishListRevisions(id);

        // Return the DTO List:
        return history.stream().map(WishListVersionResource::convert).collect(Collectors.toList());
    }

    public static WishListVersionDTO convert(WishListVersion source) {
        if (source == null) {
            return null;
        }
        WishListDTO target = source.getWishListDTO();
        Long revision = source.getRevision().longValue();
        RevisionTypeDto revisionTypeDto = Converters.convert(source.getRevisionType());
        UUID revisedBy = source.getRevisedBy();
        String revisedByType = source.getRevisedByType();
        String auditor = source.getAuditor();

        return new WishListVersionDTO(target, revision, revisionTypeDto, revisedBy, revisedByType, auditor);
    }
}
