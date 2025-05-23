package com.marketplace.elastic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.elastic.document.Product;
import com.marketplace.elastic.dto.request.ProductFilterRequestDTO;
import com.marketplace.elastic.dto.response.aggregate.ProductAggregateDTO;
import com.marketplace.elastic.dto.response.search.ProductSearchItemDTO;
import com.marketplace.elastic.manager.ProductManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductManager productManager;

    @InjectMocks
    private ProductController productController;

    private static final Long TEST_PRODUCT_ID = 1L;
    private static final String TEST_PRODUCT_TITLE = "Test Product";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getProduct_ShouldReturnProduct_WhenProductExists() throws Exception {
        Product testProduct = createTestProduct();
        when(productManager.product(TEST_PRODUCT_ID)).thenReturn(testProduct);

        mockMvc.perform(get("/products/{id}", TEST_PRODUCT_ID))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(TEST_PRODUCT_ID))
               .andExpect(jsonPath("$.title").value(TEST_PRODUCT_TITLE));
    }

    @Test
    void aggregateProducts_ShouldReturnAggregateResult() throws Exception {
        ProductFilterRequestDTO request = new ProductFilterRequestDTO();
        ProductAggregateDTO response = new ProductAggregateDTO();

        when(productManager.aggregate(any(ProductFilterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/products/aggregate")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    @Test
    void searchProducts_ShouldReturnResults_WhenProductsExist() throws Exception {
        ProductFilterRequestDTO request = new ProductFilterRequestDTO();
        ProductSearchItemDTO item = new ProductSearchItemDTO();
        List<ProductSearchItemDTO> response = Collections.singletonList(item);

        when(productManager.search(any(ProductFilterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/products/search")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchProducts_ShouldReturnEmptyArray_WhenNoProductsFound() throws Exception {
        ProductFilterRequestDTO request = new ProductFilterRequestDTO();

        when(productManager.search(any(ProductFilterRequestDTO.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/products/search")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$").isEmpty());
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(TEST_PRODUCT_ID);
        product.setTitle(TEST_PRODUCT_TITLE);
        return product;
    }
}