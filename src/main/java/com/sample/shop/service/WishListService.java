package com.sample.shop.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.sample.shop.domain.WishList;
import com.sample.shop.repository.WishListRepository;
import com.sample.shop.repository.search.WishListSearchRepository;
import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.service.mapper.WishListMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WishList}.
 */
@Service
@Transactional
public class WishListService {

    private final Logger log = LoggerFactory.getLogger(WishListService.class);

    private final WishListRepository wishListRepository;

    private final WishListMapper wishListMapper;

    private final WishListSearchRepository wishListSearchRepository;

    public WishListService(
        WishListRepository wishListRepository,
        WishListMapper wishListMapper,
        WishListSearchRepository wishListSearchRepository
    ) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
        this.wishListSearchRepository = wishListSearchRepository;
    }

    /**
     * Save a wishList.
     *
     * @param wishListDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public WishListDTO save(WishListDTO wishListDTO) {
        log.debug("Request to save WishList : {}", wishListDTO);
        WishList wishList = wishListMapper.toEntity(wishListDTO);
        wishList = wishListRepository.save(wishList);
        WishListDTO result = wishListMapper.toDto(wishList);
        // wishListSearchRepository.index(wishList);
        return result;
    }

    /**
     * Update a wishList.
     *
     * @param wishListDTO the entity to save.
     * @return the persisted entity.
     */
    public WishListDTO update(WishListDTO wishListDTO) {
        log.debug("Request to update WishList : {}", wishListDTO);
        WishList wishList = wishListMapper.toEntity(wishListDTO);
        wishList = wishListRepository.save(wishList);
        WishListDTO result = wishListMapper.toDto(wishList);
        // wishListSearchRepository.index(wishList);
        return result;
    }

    /**
     * Partially update a wishList.
     *
     * @param wishListDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WishListDTO> partialUpdate(WishListDTO wishListDTO) {
        log.debug("Request to partially update WishList : {}", wishListDTO);

        return wishListRepository
            .findById(wishListDTO.getId())
            .map(existingWishList -> {
                wishListMapper.partialUpdate(existingWishList, wishListDTO);

                return existingWishList;
            })
            .map(wishListRepository::save)
            .map(savedWishList -> {
                //   wishListSearchRepository.save(savedWishList);

                return savedWishList;
            })
            .map(wishListMapper::toDto);
    }

    /**
     * Get all the wishLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WishListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WishLists");
        return wishListRepository.findAll(pageable).map(wishListMapper::toDto);
    }

    /**
     * Get one wishList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WishListDTO> findOne(UUID id) {
        log.debug("Request to get WishList : {}", id);
        return wishListRepository.findById(id).map(wishListMapper::toDto);
    }

    /**
     * Delete the wishList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete WishList : {}", id);
        wishListRepository.deleteById(id);
        // wishListSearchRepository.deleteById(id);
    }

    /**
     * Search for the wishList corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WishListDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WishLists for query {}", query);
        return wishListSearchRepository.search(query, pageable).map(wishListMapper::toDto);
    }
}
