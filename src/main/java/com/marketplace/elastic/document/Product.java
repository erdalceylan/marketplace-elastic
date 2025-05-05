package com.marketplace.elastic.document;

import com.marketplace.elastic.document.product.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import java.util.List;

@Data
@Document(indexName = "product",  writeTypeHint = WriteTypeHint.FALSE)
public class Product {

    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text, index = false)
    private String description;

    @Field(type = FieldType.Text, index = false)
    private String slug;

    @Field(type = FieldType.Keyword)
    private String modelCode;

    @Field(type = FieldType.Float)
    private Float rate;

    @Field(type = FieldType.Object)
    private Brand brand;

    @Field(type = FieldType.Object)
    private Category category;

    @Field(type = FieldType.Nested)
    private List<Category> categories;

    @Field(type = FieldType.Object)
    private Color color;

    @Field(type = FieldType.Object)
    private State state;

    @Field(type = FieldType.Object)
    private Gender gender;

    @Field(type = FieldType.Nested)
    private List<Variant> variants;

    @Field(type = FieldType.Nested)
    private List<Image> images;

    @Field(type = FieldType.Nested)
    private List<AttributeValue> attributeValues;
}
