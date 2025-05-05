package com.marketplace.elastic.manager;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.marketplace.elastic.document.Product;
import com.marketplace.elastic.dto.request.ProductFilterRequestDTO;
import com.marketplace.elastic.dto.response.aggregate.ProductAggregateDTO;
import com.marketplace.elastic.dto.response.search.ProductSearchItemDTO;
import com.marketplace.elastic.mapper.ProductAggregationMapper;
import com.marketplace.elastic.service.ProductFilterQueryBuilderService;
import com.marketplace.elastic.service.ProductAggregateService;
import com.marketplace.elastic.service.ProductSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductManager {

    private final ProductAggregateService productAggregateService;
    private final ProductAggregationMapper productAggregationMapper;
    private final ProductFilterQueryBuilderService filterQueryBuilderService;
    private final ProductSearchService productSearchService;

    public ProductManager(
            ProductAggregateService productAggregateService,
            ProductAggregationMapper productAggregationMapper,
            ProductFilterQueryBuilderService filterQueryBuilderService,
            ProductSearchService productSearchService
    ) {
        this.productAggregateService = productAggregateService;
        this.productAggregationMapper = productAggregationMapper;
        this.filterQueryBuilderService = filterQueryBuilderService;
        this.productSearchService = productSearchService;
    }

    public Product product(Long id){
        return productSearchService.product(id);
    }

    public ProductAggregateDTO aggregate(ProductFilterRequestDTO requestDTO){

        var queryFilters = filterQueryBuilderService.build(requestDTO);
        Map<String, Aggregate> result = productAggregateService.aggregate(queryFilters);
        return productAggregationMapper.mapAggregations(result);
    }

    public List<ProductSearchItemDTO> search(ProductFilterRequestDTO requestDTO){

        var queryFilters = filterQueryBuilderService.build(requestDTO);
        return productSearchService.search(queryFilters, requestDTO.getFrom(), requestDTO.getSize());
    }
}
