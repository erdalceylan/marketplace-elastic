package com.marketplace.elastic.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductFilterRequestParentChildDTO {
    private Long id;
    private List<Long> childIds;
}
