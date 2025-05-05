package com.marketplace.elastic.dto.response.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateAttributeDTO {
    private Long id;
    private String name;
    List<AggregateAttributeValueDTO> values;
    private long count;
}
