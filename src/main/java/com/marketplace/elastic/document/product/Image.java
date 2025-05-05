package com.marketplace.elastic.document.product;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class Image {
    @Field(type = FieldType.Long)
    private Long id;
    @Field(type = FieldType.Text, index = false)
    private String root;
    @Field(type = FieldType.Text, index = false)
    private String folder;
    @Field(type = FieldType.Text, index = false)
    private String fileName;
    @Field(type = FieldType.Text, index = false)
    private String largeFileName;
}
