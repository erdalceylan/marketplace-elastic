package com.marketplace.elastic.repository;

import com.marketplace.elastic.document.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, Long> {

}