package com.sample.shop.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.sample.shop.domain.Category;
import com.sample.shop.repository.CategoryRepository;
import com.sample.shop.repository.search.CategorySearchRepository;
import com.sample.shop.service.dto.CategoryDTO;
import com.sample.shop.service.mapper.CategoryMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Category}.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final CategorySearchRepository categorySearchRepository;

    public CategoryService(
        CategoryRepository categoryRepository,
        CategoryMapper categoryMapper,
        CategorySearchRepository categorySearchRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categorySearchRepository = categorySearchRepository;
    }

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = categoryMapper.toDto(category);
        // categorySearchRepository.index(category);
        return result;
    }

    /**
     * Update a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryDTO update(CategoryDTO categoryDTO) {
        log.debug("Request to update Category : {}", categoryDTO);
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = categoryMapper.toDto(category);
        // categorySearchRepository.index(category);
        return result;
    }

    /**
     * Partially update a category.
     *
     * @param categoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryDTO> partialUpdate(CategoryDTO categoryDTO) {
        log.debug("Request to partially update Category : {}", categoryDTO);

        return categoryRepository
            .findById(categoryDTO.getId())
            .map(existingCategory -> {
                categoryMapper.partialUpdate(existingCategory, categoryDTO);

                return existingCategory;
            })
            .map(categoryRepository::save)
            .map(savedCategory -> {
                //    categorySearchRepository.save(savedCategory);

                return savedCategory;
            })
            .map(categoryMapper::toDto);
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    /**
     * Get all the categories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return categoryRepository.findAllWithEagerRelationships(pageable).map(categoryMapper::toDto);
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findOne(UUID id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findOneWithEagerRelationships(id).map(categoryMapper::toDto);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
        // categorySearchRepository.deleteById(id);
    }

    /**
     * Search for the category corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categories for query {}", query);
        return categorySearchRepository.search(query, pageable).map(categoryMapper::toDto);
    }
}
