package com.marketplace.elastic.mapper;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.TopHitsAggregate;
import com.marketplace.elastic.dto.response.aggregate.*;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductAggregationMapper {

    public ProductAggregateDTO mapAggregations(Map<String, Aggregate> aggregations) {
        ProductAggregateDTO dto = new ProductAggregateDTO();

        // Brands
        if (aggregations.containsKey("brands")) {
            LongTermsAggregate brandsAgg = aggregations.get("brands").lterms();
            dto.setBrands(brandsAgg.buckets().array().stream()
                    .map(bucket -> {
                        String name = extractTopHitValue(bucket.aggregations().get("name"), "brand.name");
                        return new AggregateBrandDTO(
                                bucket.key(),
                                name,
                                bucket.docCount()
                        );
                    })
                    .collect(Collectors.toList()));
        }

        // Colors
        if (aggregations.containsKey("colors")) {
            LongTermsAggregate colorsAgg = aggregations.get("colors").lterms();
            dto.setColors(colorsAgg.buckets().array().stream()
                    .map(bucket -> {
                        String name = extractTopHitValue(bucket.aggregations().get("name"), "color.name");
                        return new AggregateColorDTO(
                                bucket.key(),
                                name,
                                bucket.docCount()
                        );
                    })
                    .collect(Collectors.toList()));
        }

        // Genders
        if (aggregations.containsKey("genders")) {
            LongTermsAggregate gendersAgg = aggregations.get("genders").lterms();
            dto.setGenders(gendersAgg.buckets().array().stream()
                    .map(bucket -> {
                        String name = extractTopHitValue(bucket.aggregations().get("name"), "gender.name");
                        return new AggregateGenderDTO(
                                bucket.key(),
                                name,
                                bucket.docCount()
                        );
                    })
                    .collect(Collectors.toList()));
        }

        // Categories
        if (aggregations.containsKey("categories")) {
            Aggregate categoriesAgg = aggregations.get("categories");
            NestedAggregate nestedCategories = categoriesAgg.nested();
            LongTermsAggregate categoryTerms = nestedCategories.aggregations().get("category").lterms();
            dto.setCategories(categoryTerms.buckets().array().stream()
                    .map(bucket -> {
                        String name = extractTopHitValue(bucket.aggregations().get("name"), "name");
                        return new AggregateCategoryDTO(
                                bucket.key(),
                                name,
                                bucket.docCount()
                        );
                    })
                    .collect(Collectors.toList()));
        }

        // Options
        if (aggregations.containsKey("options")) {
            Aggregate optionsAgg = aggregations.get("options");
            NestedAggregate nestedOptions = optionsAgg.nested();
            LongTermsAggregate optionTerms = nestedOptions.aggregations().get("option").lterms();
            dto.setOptions(optionTerms.buckets().array().stream()
                    .map(bucket -> {
                        String optionName = extractTopHitValue(bucket.aggregations().get("name"), "optionValue.option.name");

                        // Option Values
                        LongTermsAggregate valuesAgg = bucket.aggregations().get("values").lterms();
                        List<AggregateOptionValueDTO> values = valuesAgg.buckets().array().stream()
                                .map(valueBucket -> {
                                    String valueName = extractTopHitValue(valueBucket.aggregations().get("name"), "optionValue.name");
                                    return new AggregateOptionValueDTO(
                                            valueBucket.key(),
                                            valueName,
                                            valueBucket.docCount()
                                    );
                                })
                                .collect(Collectors.toList());

                        return new AggregateOptionDTO(
                                bucket.key(),
                                optionName,
                                values,
                                bucket.docCount()
                        );
                    })
                    .collect(Collectors.toList()));
        }

        // Attributes
        if (aggregations.containsKey("attributes")) {
            Aggregate attributesAgg = aggregations.get("attributes");
            NestedAggregate nestedAttributes = attributesAgg.nested();
            LongTermsAggregate attributeTerms = nestedAttributes.aggregations().get("attribute").lterms();
            dto.setAttributes(attributeTerms.buckets().array().stream()
                    .filter(bucket -> {
                        String attributeName = extractTopHitValue(bucket.aggregations().get("name"), "attribute.name");
                        return !"renk".equals(attributeName.trim().toLowerCase(Locale.ROOT));
                    })
                    .map(bucket -> {
                        String attributeName = extractTopHitValue(bucket.aggregations().get("name"), "attribute.name");

                        // Attribute Values
                        LongTermsAggregate valuesAgg = bucket.aggregations().get("values").lterms();
                        List<AggregateAttributeValueDTO> values = valuesAgg.buckets().array().stream()
                                .map(valueBucket -> {
                                    String valueName = extractTopHitValue(valueBucket.aggregations().get("name"), "name");
                                    return new AggregateAttributeValueDTO(
                                            valueBucket.key(),
                                            valueName,
                                            valueBucket.docCount()
                                    );
                                })
                                .collect(Collectors.toList());

                        return new AggregateAttributeDTO(
                                bucket.key(),
                                attributeName,
                                values,
                                bucket.docCount()
                        );
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private String extractTopHitValue(Aggregate aggregate, String sourceField) {
        if (aggregate == null) return null;

        // TopHitsAggregate kontrolü
        TopHitsAggregate topHits = aggregate.topHits();
        if (topHits == null || topHits.hits().hits().isEmpty()) return null;

        // İlk hit'in kaynağını al
        JsonData source1 =  topHits.hits().hits().get(0).source();
        if (source1 == null) return null;
        JsonNode source = source1.to(JsonNode.class);
        if (source == null) return null;

        // Örnek: "brand.name" -> "/brand/name"
        String jsonPath = "/" + sourceField.replace(".", "/");
        return source.at(jsonPath).asText(null);
    }

}

