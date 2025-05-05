package com.marketplace.elastic.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.elastic.document.Product;
import com.marketplace.elastic.service.ProductElasticBulkService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {


    private final ObjectMapper objectMapper;
    private final ProductElasticBulkService productElasticBulkService;


    public ProductConsumer(
            ObjectMapper objectMapper,
            ProductElasticBulkService productElasticBulkService
    ) {
        this.objectMapper = objectMapper;
        this.productElasticBulkService = productElasticBulkService;
    }

    @KafkaListener(topics = "${kafka.product-topic}", groupId = "${kafka.elastic-update-group}")
    public void consume(String message) {
        try {

            Product product = objectMapper.readValue(message, Product.class);
            productElasticBulkService.addForBulk(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
