package com.marketplace.elastic.dto.request;

import lombok.Data;

@Data
public class ProductFilterRequestRangeDTO {
    private Long min;
    private Long max;
}
