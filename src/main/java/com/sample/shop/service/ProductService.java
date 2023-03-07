package com.sample.shop.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.sample.shop.domain.Product;
import com.sample.shop.repository.ProductRepository;
import com.sample.shop.repository.search.ProductSearchRepository;
import com.sample.shop.service.dto.ProductDTO;
import com.sample.shop.service.mapper.ProductMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductService(
        ProductRepository productRepository,
        ProductMapper productMapper,
        ProductSearchRepository productSearchRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        // productSearchRepository.index(product);
        return result;
    }

    /**
     * Update a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO update(ProductDTO productDTO) {
        log.debug("Request to update Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        // productSearchRepository.index(product);
        return result;
    }

    /**
     * Partially update a product.
     *
     * @param productDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(savedProduct -> {
                //    productSearchRepository.save(savedProduct);

                return savedProduct;
            })
            .map(productMapper::toDto);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(UUID id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
        // productSearchRepository.deleteById(id);
    }

    /**
     * Search for the product corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productSearchRepository.search(query, pageable).map(productMapper::toDto);
    }
}
