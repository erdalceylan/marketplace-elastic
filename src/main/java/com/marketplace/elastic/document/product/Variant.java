package com.marketplace.elastic.document.product;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
public class Variant {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String barcode;

    @Field(type = FieldType.Object)
    private VariantOptionValue optionValue;

    @Field(type = FieldType.Nested)
    private List<MerchantVariant> merchantVariants;
}
