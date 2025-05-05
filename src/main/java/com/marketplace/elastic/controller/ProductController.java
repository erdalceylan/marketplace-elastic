package com.marketplace.elastic.controller;

import com.marketplace.elastic.document.Product;
import com.marketplace.elastic.dto.request.ProductFilterRequestDTO;
import com.marketplace.elastic.dto.response.aggregate.ProductAggregateDTO;
import com.marketplace.elastic.dto.response.search.ProductSearchItemDTO;
import com.marketplace.elastic.manager.ProductManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductManager productManager;

    public ProductController(ProductManager productManager) {
        this.productManager = productManager;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> product(@PathVariable Long id) {

        return ResponseEntity.ok(productManager.product(id));
    }

    @PostMapping("/aggregate")
    public ResponseEntity<ProductAggregateDTO> aggregate(@RequestBody ProductFilterRequestDTO requestDTO) {

        return ResponseEntity.ok(productManager.aggregate(requestDTO));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProductSearchItemDTO>> search(@RequestBody ProductFilterRequestDTO requestDTO) {

        return ResponseEntity.ok(productManager.search(requestDTO));
    }
}
