package com.marketplace.elastic.repository;

import com.marketplace.elastic.document.Suggest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestRepository extends ElasticsearchRepository<Suggest, String> {

}