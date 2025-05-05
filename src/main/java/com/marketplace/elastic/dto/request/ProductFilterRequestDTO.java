package com.marketplace.elastic.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProductFilterRequestDTO {
    private Integer from;
    private Integer size;
    private String q;
    private List<Long> categoryIds;
    private List<Long> brandIds;
    private List<Long> colorIds;
    private List<Long> genderIds;
    private ProductFilterRequestRangeDTO price;
    private List<ProductFilterRequestParentChildDTO> attributes;
    private List<ProductFilterRequestParentChildDTO> options;
}
