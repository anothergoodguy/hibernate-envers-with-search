package com.sample.shop.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.sample.shop.repository.WishListRepository;
import com.sample.shop.service.WishListService;
import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sample.shop.domain.WishList}.
 */
@RestController
@RequestMapping("/api")
public class WishListResource {

    private final Logger log = LoggerFactory.getLogger(WishListResource.class);

    private static final String ENTITY_NAME = "wishList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WishListService wishListService;

    private final WishListRepository wishListRepository;

    public WishListResource(WishListService wishListService, WishListRepository wishListRepository) {
        this.wishListService = wishListService;
        this.wishListRepository = wishListRepository;
    }

    /**
     * {@code POST  /wish-lists} : Create a new wishList.
     *
     * @param wishListDTO the wishListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wishListDTO, or with status {@code 400 (Bad Request)} if the wishList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wish-lists")
    public ResponseEntity<WishListDTO> createWishList(@Valid @RequestBody WishListDTO wishListDTO) throws URISyntaxException {
        log.debug("REST request to save WishList : {}", wishListDTO);
        if (wishListDTO.getId() != null) {
            throw new BadRequestAlertException("A new wishList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WishListDTO result = wishListService.save(wishListDTO);
        return ResponseEntity
            .created(new URI("/api/wish-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wish-lists/:id} : Updates an existing wishList.
     *
     * @param id the id of the wishListDTO to save.
     * @param wishListDTO the wishListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishListDTO,
     * or with status {@code 400 (Bad Request)} if the wishListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wishListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wish-lists/{id}")
    public ResponseEntity<WishListDTO> updateWishList(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody WishListDTO wishListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WishList : {}, {}", id, wishListDTO);
        if (wishListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WishListDTO result = wishListService.update(wishListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wishListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wish-lists/:id} : Partial updates given fields of an existing wishList, field will ignore if it is null
     *
     * @param id the id of the wishListDTO to save.
     * @param wishListDTO the wishListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishListDTO,
     * or with status {@code 400 (Bad Request)} if the wishListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the wishListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the wishListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wish-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WishListDTO> partialUpdateWishList(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody WishListDTO wishListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WishList partially : {}, {}", id, wishListDTO);
        if (wishListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WishListDTO> result = wishListService.partialUpdate(wishListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wishListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wish-lists} : get all the wishLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wishLists in body.
     */
    @GetMapping("/wish-lists")
    public ResponseEntity<List<WishListDTO>> getAllWishLists(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WishLists");
        Page<WishListDTO> page = wishListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wish-lists/:id} : get the "id" wishList.
     *
     * @param id the id of the wishListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wishListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wish-lists/{id}")
    public ResponseEntity<WishListDTO> getWishList(@PathVariable UUID id) {
        log.debug("REST request to get WishList : {}", id);
        Optional<WishListDTO> wishListDTO = wishListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wishListDTO);
    }

    /**
     * {@code DELETE  /wish-lists/:id} : delete the "id" wishList.
     *
     * @param id the id of the wishListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wish-lists/{id}")
    public ResponseEntity<Void> deleteWishList(@PathVariable UUID id) {
        log.debug("REST request to delete WishList : {}", id);
        wishListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/wish-lists?query=:query} : search for the wishList corresponding
     * to the query.
     *
     * @param query the query of the wishList search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/wish-lists")
    public ResponseEntity<List<WishListDTO>> searchWishLists(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of WishLists for query {}", query);
        Page<WishListDTO> page = wishListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
