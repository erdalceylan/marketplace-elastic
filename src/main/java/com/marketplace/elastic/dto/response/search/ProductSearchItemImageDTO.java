package com.marketplace.elastic.dto.response.search;


import lombok.Data;

@Data
public class ProductSearchItemImageDTO {

    public ProductSearchItemImageDTO(){}

    private Long id;
    private String root;
    private String folder;
    private String fileName;
    private String largeFileName;

}
