package com.marketplace.elastic.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.elastic.document.Suggest;
import com.marketplace.elastic.service.SuggestElasticBulkService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SuggestConsumer {


    private final ObjectMapper objectMapper;
    private final SuggestElasticBulkService suggestElasticBulkService;


    public SuggestConsumer(
            ObjectMapper objectMapper,
            SuggestElasticBulkService suggestElasticBulkService
    ) {
        this.objectMapper = objectMapper;
        this.suggestElasticBulkService = suggestElasticBulkService;
    }

    @KafkaListener(topics = "${kafka.suggest-topic}", groupId = "${kafka.elastic-update-group}")
    public void consume(String message) {
        try {

            Suggest suggest = objectMapper.readValue(message, Suggest.class);
            suggestElasticBulkService.addForBulk(suggest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
