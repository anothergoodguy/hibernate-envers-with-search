package com.sample.shop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.sample.shop.domain.WishList;
import com.sample.shop.repository.WishListRepository;
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
 * Spring Data Elasticsearch repository for the {@link WishList} entity.
 */
public interface WishListSearchRepository extends ElasticsearchRepository<WishList, UUID>, WishListSearchRepositoryInternal {}

interface WishListSearchRepositoryInternal {
    Page<WishList> search(String query, Pageable pageable);

    Page<WishList> search(Query query);

    void index(WishList entity);
}

class WishListSearchRepositoryInternalImpl implements WishListSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final WishListRepository repository;

    WishListSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, WishListRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<WishList> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<WishList> search(Query query) {
        SearchHits<WishList> searchHits = elasticsearchTemplate.search(query, WishList.class);
        List<WishList> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(WishList entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
