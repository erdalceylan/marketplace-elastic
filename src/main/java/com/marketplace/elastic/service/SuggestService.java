package com.marketplace.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.marketplace.elastic.document.Suggest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestService {

    private final ElasticsearchClient elasticsearchClient;

    public SuggestService(
            ElasticsearchClient elasticsearchClient
    ){
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Suggest> suggest(String q, Integer limit){
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("suggest")
                .from(0)
                .size(limit)
                .query(q2 -> q2
                        .bool(b -> b
                                .should(sh -> sh
                                        .match(m -> m
                                                .field("text")
                                                .query(q)
                                                .boost(2.0f)
                                        )
                                )
                                .should(sh -> sh
                                        .match(m -> m
                                                .field("text.fuzzy")
                                                .query(q)
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                                .boost(1.0f)
                                        )
                                )
                        )
                )
                .sort(so -> so
                        .score(sco -> sco.order(SortOrder.Desc))
                )
        );

        System.out.println(searchRequest);
        try {
            return elasticsearchClient
                    .search(searchRequest, Suggest.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
