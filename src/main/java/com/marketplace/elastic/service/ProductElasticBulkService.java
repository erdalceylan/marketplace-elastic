package com.marketplace.elastic.service;

import com.marketplace.elastic.document.Product;
import com.marketplace.elastic.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ProductElasticBulkService {

    private final ProductRepository productRepository;
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    private ScheduledFuture<?> scheduledTask;
    @Value("${elastic.product-bulk-size}")
    private int BULK_SIZE;
    @Value("${elastic.product-bulk-second}")
    private int TIMEOUT_SECOND;


    public ProductElasticBulkService(
            ProductRepository productRepository
    ){
        this.productRepository = productRepository;
        scheduleNext();
    }

    public void addForBulk(Product product){
        this.products.put(product.getId(), product);

        if (products.size() >= BULK_SIZE) {
            sendBulk();
        }
    }

    private void sendBulk(){
        if (!products.isEmpty()) {
            System.out.println("Product Elasticsearch'e kaydedildi: " + products.size());
            productRepository.saveAll(products.values());
            products.clear();
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
