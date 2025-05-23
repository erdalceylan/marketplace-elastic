package com.marketplace.elastic.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.marketplace.elastic.dto.request.ProductFilterRequestDTO;
import com.marketplace.elastic.dto.request.ProductFilterRequestParentChildDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.List;

@Service
public class ProductFilterQueryBuilderService {

    public BoolQuery build(ProductFilterRequestDTO requestDTO){

        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        if (StringUtils.hasText(requestDTO.getQ())) {
            boolQuery.must(Query.of(q -> q
                    .match(m -> m
                            .field("title")
                            .query(requestDTO.getQ())
                            .fuzziness("AUTO")
                    )
            ));
        }


        if (StringUtils.hasText(requestDTO.getModelCode())) {
            boolQuery.filter(Query.of(q -> q
                    .term(t -> t
                            .field("modelCode")
                            .value(ts -> ts.stringValue(requestDTO.getModelCode().trim()))
                    )
            ));
        }

        if (!CollectionUtils.isEmpty(requestDTO.getBrands())) {
            boolQuery.filter(Query.of(q -> q
                    .terms(t -> t
                            .field("brand.id")
                            .terms(ts -> ts.value(convertToTerms(requestDTO.getBrands())))
                    )
            ));
        }

        if (!CollectionUtils.isEmpty(requestDTO.getColors())) {
            boolQuery.filter(Query.of(q -> q
                    .terms(t -> t
                            .field("color.id")
                            .terms(ts -> ts.value(convertToTerms(requestDTO.getColors())))
                    )
            ));
        }

        if (!CollectionUtils.isEmpty(requestDTO.getGenders())) {
            boolQuery.filter(Query.of(q -> q
                    .terms(t -> t
                            .field("gender.id")
                            .terms(ts -> ts.value(convertToTerms(requestDTO.getGenders())))
                    )
            ));
        }

        if (requestDTO.getPrice() != null && (requestDTO.getPrice().getMin() != null || requestDTO.getPrice().getMax() != null)) {
            boolQuery.filter(Query.of(q -> q
                    .nested(n -> n
                            .path("variants")
                            .query(nq -> nq
                                    .nested(n2-> n2
                                            .path("variants.merchantVariants")
                                            .query(nq2->nq2.range(r -> r.number(nqb -> nqb
                                                    .field("variants.merchantVariants.discountedPrice")
                                                    .gte(requestDTO.getPrice().getMin() != null ? requestDTO.getPrice().getMin().doubleValue() : null)
                                                    .lte(requestDTO.getPrice().getMax() != null ? requestDTO.getPrice().getMax().doubleValue() : null))
                                            )))

                            )
                    )
            ));
        }

        if (!CollectionUtils.isEmpty(requestDTO.getCategories())) {
            boolQuery.filter(Query.of(q -> q
                    .nested(n -> n
                            .path("categories")
                            .query(nq -> nq
                                    .terms(t -> t
                                            .field("categories.id")
                                            .terms(ts -> ts.value(convertToTerms(requestDTO.getCategories())))
                                    )
                            )
                    )
            ));
        }

        if (!CollectionUtils.isEmpty(requestDTO.getAttributes())) {
            for (ProductFilterRequestParentChildDTO attr : requestDTO.getAttributes()) {
                boolQuery.filter(Query.of(q -> q
                        .nested(n -> n
                                .path("attributeValues")
                                .query(nq -> nq
                                        .bool(b -> b
                                                .must(
                                                        Query.of(q2 -> q2.term(t -> t.field("attributeValues.attribute.id").value(attr.getId()))),
                                                        Query.of(q2 -> q2.terms(t -> t.field("attributeValues.id").terms(ts -> ts.value(convertToTerms(attr.getChildIds())))))
                                                )
                                        )
                                )
                        )
                ));
            }
        }

        if (!CollectionUtils.isEmpty(requestDTO.getOptions())) {
            for (ProductFilterRequestParentChildDTO attr : requestDTO.getOptions()) {
                boolQuery.filter(Query.of(q -> q
                        .nested(n -> n
                                .path("variants")
                                .query(nq -> nq
                                        .bool(b -> b
                                                .must(
                                                        Query.of(q2 -> q2.term(t -> t.field("variants.optionValue.option.id").value(attr.getId()))),
                                                        Query.of(q2 -> q2.terms(t -> t.field("variants.optionValue.id").terms(ts -> ts.value(convertToTerms(attr.getChildIds())))))
                                                )
                                        )
                                )
                        )
                ));
            }
        }

        return boolQuery.build();
    }

    private List<FieldValue> convertToTerms(List<Long> ids) {
        return ids.stream()
                .map(id -> FieldValue.of(id.toString())).toList();
    }
}
