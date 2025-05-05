package com.marketplace.elastic.dto.response.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateGenderDTO {
    private Long id;
    private String name;
    private long count;
}
