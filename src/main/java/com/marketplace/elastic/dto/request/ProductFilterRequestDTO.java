package com.marketplace.elastic.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProductFilterRequestDTO {
    private Integer from;
    private Integer size;
    private String q;
    private String modelCode;
    private List<Long> categories;
    private List<Long> brands;
    private List<Long> merchants;
    private List<Long> colors;
    private List<Long> genders;
    private ProductFilterRequestRangeDTO price;
    private List<ProductFilterRequestParentChildDTO> attributes;
    private List<ProductFilterRequestParentChildDTO> options;
}
