package com.marketplace.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.elastic.document.Product;
import com.marketplace.elastic.dto.response.search.ProductSearchItemDTO;
import com.marketplace.elastic.dto.response.search.ProductSearchItemImageDTO;
import com.marketplace.elastic.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public ProductSearchService(
            ElasticsearchClient elasticsearchClient,
            ObjectMapper objectMapper,
            ProductRepository productRepository
    ){
        this.elasticsearchClient = elasticsearchClient;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    public Product product(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<ProductSearchItemDTO> search(BoolQuery boolQuery, Integer from, Integer size) {

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("product")
                .from(from)
                .size(size)
                .query(q -> q.bool(boolQuery))
                .source(src -> src
                        .filter(f -> f
                                .includes("id", "slug", "title", "brand", "color", "rate")
                        )
                )
                .scriptFields("images", sf -> sf
                        .script(script -> script
                                .source( "if (params._source.images != null) { return params._source.images.stream().limit(4).collect(Collectors.toList()); } return [];")
                        )
                )
                .scriptFields("discountedPrice", sf -> sf
                        .script(script -> script
                                .source( """
                                              double minPrice = Double.POSITIVE_INFINITY;
                                              for (def variant : params._source.variants) {
                                                for (def merchantVariant : variant.merchantVariants) {
                                                  if (merchantVariant.discountedPrice != null) {
                                                    minPrice = Math.min(minPrice, merchantVariant.discountedPrice);
                                                  }
                                                }
                                              }
                                              return minPrice == Double.POSITIVE_INFINITY ? null : minPrice;
                                            """)
                        )
                ));

        try {
            System.out.println(searchRequest);
            return elasticsearchClient.search(searchRequest, ProductSearchItemDTO.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(a->{

                        if(a.source() != null){
                            List<?> imageList = a.fields().get("images").to(List.class);

                            List<ProductSearchItemImageDTO> images = imageList.stream()
                                    .map(fieldValue -> objectMapper.convertValue(fieldValue, ProductSearchItemImageDTO.class)).toList();

                            a.source().setImages(images);

                            List<?> discountedPriceList = a.fields().get("discountedPrice").to(List.class);
                            Float discountedPrice = discountedPriceList.stream().map(fieldValue->objectMapper.convertValue(fieldValue, Float.class)).toList().getFirst();
                            a.source().setDiscountedPrice(discountedPrice);
                        }

                        return a.source();
                    })
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
