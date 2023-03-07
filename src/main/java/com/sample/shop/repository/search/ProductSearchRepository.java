package com.sample.shop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.sample.shop.domain.Product;
import com.sample.shop.repository.ProductRepository;
import java.util.List;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Product} entity.
 */
public interface ProductSearchRepository extends ElasticsearchRepository<Product, UUID>, ProductSearchRepositoryInternal {}

interface ProductSearchRepositoryInternal {
    Page<Product> search(String query, Pageable pageable);

    Page<Product> search(Query query);

    void index(Product entity);
}

class ProductSearchRepositoryInternalImpl implements ProductSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ProductRepository repository;

    ProductSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, ProductRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Product> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Product> search(Query query) {
        SearchHits<Product> searchHits = elasticsearchTemplate.search(query, Product.class);
        List<Product> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Product entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
