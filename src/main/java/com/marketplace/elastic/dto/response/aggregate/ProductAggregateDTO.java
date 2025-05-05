package com.marketplace.elastic.dto.response.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregateDTO {
    private List<AggregateBrandDTO> brands;
    private List<AggregateColorDTO> colors;
    private List<AggregateGenderDTO> genders;
    private List<AggregateCategoryDTO> categories;
    private List<AggregateOptionDTO> options;
    private List<AggregateAttributeDTO> attributes;
}
