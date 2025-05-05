package com.marketplace.elastic.dto.response.search;

import lombok.Data;

import java.util.List;

@Data
public class ProductSearchItemDTO {
    private Long id;
    private String title;
    private String slug;
    private Float rate;
    private Float discountedPrice;
    private ProductSearchItemBrandDTO brand;
    private ProductSearchItemColorDTO color;
    private List<ProductSearchItemImageDTO> images;
}
