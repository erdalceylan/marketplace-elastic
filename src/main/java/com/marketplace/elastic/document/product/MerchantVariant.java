package com.marketplace.elastic.document.product;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class MerchantVariant {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Object)
    private Merchant merchant;

    @Field(type = FieldType.Float)
    private Float originalPrice;

    @Field(type = FieldType.Float)
    private Float discountedPrice;

    @Field(type = FieldType.Integer)
    private Integer stockQuantity;
}
