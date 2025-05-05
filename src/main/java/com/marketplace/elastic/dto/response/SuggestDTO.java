package com.marketplace.elastic.dto.response;

import lombok.Data;

@Data
public class SuggestDTO {
    private String id;
    private String text;
    private String type;
    private Long refId;
}
