package com.marketplace.elastic.document.product;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class Merchant {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text, index = false)
    private String officialName;

    @Field(type = FieldType.Float)
    private Float rate;

    @Field(type = FieldType.Object)
    private Image logoImage;
}
