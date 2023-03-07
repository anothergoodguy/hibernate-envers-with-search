package com.sample.shop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.sample.shop.domain.Address;
import com.sample.shop.repository.AddressRepository;
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
 * Spring Data Elasticsearch repository for the {@link Address} entity.
 */
public interface AddressSearchRepository extends ElasticsearchRepository<Address, UUID>, AddressSearchRepositoryInternal {}

interface AddressSearchRepositoryInternal {
    Page<Address> search(String query, Pageable pageable);

    Page<Address> search(Query query);

    void index(Address entity);
}

class AddressSearchRepositoryInternalImpl implements AddressSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final AddressRepository repository;

    AddressSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, AddressRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Address> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Address> search(Query query) {
        SearchHits<Address> searchHits = elasticsearchTemplate.search(query, Address.class);
        List<Address> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Address entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
