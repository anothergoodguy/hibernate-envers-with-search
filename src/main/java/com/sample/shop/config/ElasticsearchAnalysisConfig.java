package com.sample.shop.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class ElasticsearchAnalysisConfig implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context
            .analyzer("tm_english")
            .custom()
            .tokenizer("standard")
            .tokenFilters("lowercase", "snowball_english", "asciifolding", "custom_word_delimiter");

        context.tokenFilter("snowball_english").type("snowball").param("language", "English");

        context
            .tokenFilter("custom_word_delimiter")
            .type("word_delimiter")
            .param("language", "English")
            .param("split_on_numerics", false)
            .param("generate_word_parts", false)
            .param("catenate_words", true)
            .param("generate_number_parts", false)
            .param("catenate_all", true)
            .param("split_on_case_change", false)
            .param("catenate_numbers", false)
            .param("preserve_original", true);

        context.analyzer("name").custom().tokenizer("standard").tokenFilters("lowercase", "asciifolding");

        context.analyzer("tm_keyword_analyzer").custom().tokenizer("keyword").tokenFilters("lowercase", "asciifolding");

        context.normalizer("case_insensitive_normalizer").custom().tokenFilters("asciifolding", "lowercase");

        context
            .analyzer("autocomplete_indexing")
            .custom()
            .tokenizer("whitespace")
            .charFilters("tm_char_filter")
            .tokenFilters("lowercase", "asciifolding", "autocomplete_edge_ngram");

        context.tokenFilter("autocomplete_edge_ngram").type("edge_ngram").param("min_gram", 1).param("max_gram", 40);

        // Same as "autocomplete_indexing", but without the edge-ngram filter

        context
            .analyzer("autocomplete_search")
            .custom()
            .tokenizer("whitespace")
            .charFilters("tm_char_filter")
            .tokenFilters("lowercase", "asciifolding");

        context.charFilter("tm_char_filter").type("mapping").param("mappings", "- => ", "+ => ", "( => ", ") => ", "+91 => ");
    }
}
