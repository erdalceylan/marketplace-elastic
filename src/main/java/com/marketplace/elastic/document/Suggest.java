package com.marketplace.elastic.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "suggest",  writeTypeHint = WriteTypeHint.FALSE)
@Setting(settingPath = "/elasticsearch/suggest-settings.json")
@Mapping(mappingPath = "/elasticsearch/suggest-mapping.json")
public class Suggest {

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "autosuggest_analyzer"),
            otherFields = {
                    @InnerField(suffix = "fuzzy", type = FieldType.Text, analyzer = "standard"),
                    @InnerField(suffix = "exact", type = FieldType.Keyword)
            }
    )
    private String text;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Long)
    private Long refId;
}
