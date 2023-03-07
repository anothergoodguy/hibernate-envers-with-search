package com.sample.shop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.sample.shop.domain.Category;
import com.sample.shop.repository.CategoryRepository;
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
 * Spring Data Elasticsearch repository for the {@link Category} entity.
 */
public interface CategorySearchRepository extends ElasticsearchRepository<Category, UUID>, CategorySearchRepositoryInternal {}

interface CategorySearchRepositoryInternal {
    Page<Category> search(String query, Pageable pageable);

    Page<Category> search(Query query);

    void index(Category entity);
}

class CategorySearchRepositoryInternalImpl implements CategorySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final CategoryRepository repository;

    CategorySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, CategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Category> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Category> search(Query query) {
        SearchHits<Category> searchHits = elasticsearchTemplate.search(query, Category.class);
        List<Category> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Category entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
