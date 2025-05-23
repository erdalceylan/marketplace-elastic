package com.marketplace.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductAggregateService {

    private final ElasticsearchClient elasticsearchClient;

    public ProductAggregateService(
            ElasticsearchClient elasticsearchClient
    ){
        this.elasticsearchClient = elasticsearchClient;
    }

    public Map<String, Aggregate> aggregate(BoolQuery boolQuery) {

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("product")
                .size(0)
                .query(q -> q.bool(boolQuery))
                .aggregations("brands", a -> a
                        .terms(t -> t
                                .field("brand.id")
                                .size(150)
                        )
                        .aggregations("name", aa -> aa
                                .topHits(th -> th
                                        .size(1)
                                        .source(sr -> sr.filter(sf -> sf.includes("brand.name")))
                                )
                        )
                )
                .aggregations("colors", a -> a
                        .terms(t -> t
                                .field("color.id")
                                .size(100)
                        )
                        .aggregations("name", aa -> aa
                                .topHits(th -> th
                                        .size(1)
                                        .source(sr -> sr.filter(sf -> sf.includes("color.name")))
                                )
                        )
                )
                .aggregations("genders", a -> a
                        .terms(t -> t
                                .field("gender.id")
                                .size(10)
                        )
                        .aggregations("name", aa -> aa
                                .topHits(th -> th
                                        .size(1)
                                        .source(sr -> sr.filter(sf -> sf.includes("gender.name")))
                                )
                        )
                )
                .aggregations("categories", a -> a
                        .nested(n -> n
                                .path("categories")
                        )
                        .aggregations("category", aa -> aa
                                .terms(t -> t
                                        .field("categories.id")
                                        .size(150)
                                )
                                .aggregations("name", aaa -> aaa
                                        .topHits(th -> th
                                                .size(1)
                                                .source(sr -> sr.filter(sf -> sf.includes("categories.name")))
                                        )
                                )
                        )
                )
                .aggregations("options", a -> a
                        .nested(n -> n
                                .path("variants")
                        )
                        .aggregations("option", aa -> aa
                                .terms(t -> t
                                        .field("variants.optionValue.option.id")
                                        .size(10)
                                )
                                .aggregations("name", aaa -> aaa
                                        .topHits(th -> th
                                                .size(1)
                                                .source(sr -> sr.filter(sf -> sf.includes("variants.optionValue.option.name")))
                                        )
                                )
                                .aggregations("values", aaa -> aaa
                                        .terms(t -> t
                                                .field("variants.optionValue.id")
                                                .size(150)
                                        )
                                        .aggregations("name", aaaa -> aaaa
                                                .topHits(th -> th
                                                        .size(1)
                                                        .source(sr -> sr.filter(sf -> sf.includes("variants.optionValue.name")))
                                                )
                                        )
                                )
                        )
                )
                .aggregations("attributes", a -> a
                        .nested(n -> n
                                .path("attributeValues")
                        )
                        .aggregations("attribute", aa -> aa
                                .terms(t -> t
                                        .field("attributeValues.attribute.id")
                                        .size(5)
                                )
                                .aggregations("name", aaa -> aaa
                                        .topHits(th -> th
                                                .size(1)
                                                .source(sr -> sr.filter(sf -> sf.includes("attributeValues.attribute.name")))
                                        )
                                )
                                .aggregations("values", aaa -> aaa
                                        .terms(t -> t
                                                .field("attributeValues.id")
                                                .size(150)
                                        )
                                        .aggregations("name", aaaa -> aaaa
                                                .topHits(th -> th
                                                        .size(1)
                                                        .source(sr -> sr.filter(sf -> sf.includes("attributeValues.name")))
                                                )
                                        )
                                )
                        )
                )
        );

        try {
            return elasticsearchClient.search(searchRequest, Object.class).aggregations();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
