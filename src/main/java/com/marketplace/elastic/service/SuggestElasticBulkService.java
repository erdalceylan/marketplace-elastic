package com.marketplace.elastic.service;

import com.marketplace.elastic.document.Suggest;
import com.marketplace.elastic.repository.SuggestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class SuggestElasticBulkService {

    private final SuggestRepository suggestRepository;
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<String, Suggest> suggests = new ConcurrentHashMap<>();
    private ScheduledFuture<?> scheduledTask;
    @Value("${elastic.suggest-bulk-size}")
    private int BULK_SIZE;
    @Value("${elastic.suggest-bulk-second}")
    private int TIMEOUT_SECOND;


    public SuggestElasticBulkService(
            SuggestRepository suggestRepository
    ){
        this.suggestRepository = suggestRepository;
        scheduleNext();
    }

    public void addForBulk(Suggest suggest){
        this.suggests.put(suggest.getId(), suggest);

        if (suggests.size() >= BULK_SIZE) {
            sendBulk();
        }
    }

    private void sendBulk(){
        if (!suggests.isEmpty()) {
            System.out.println("Suggest Elasticsearch'e kaydedildi: " + suggests.size());
            suggestRepository.saveAll(suggests.values());
            suggests.clear();
        }

        scheduleNext();
    }

    private void scheduleNext() {
        if (scheduledTask != null && !scheduledTask.isDone() && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
        scheduledTask = scheduledExecutor.schedule(
                this::sendBulk,
                TIMEOUT_SECOND,
                TimeUnit.SECONDS
        );
    }
}
