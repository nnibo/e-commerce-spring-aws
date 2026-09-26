package com.nicolas.orderservice.controller;

import com.nicolas.orderservice.dto.request.ProductRequestDTO;
import com.nicolas.orderservice.dto.response.ProductResponseDTO;
import com.nicolas.orderservice.entity.ProductEntity;
import com.nicolas.orderservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        productService.createProduct(productRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable UUID id, @RequestBody ProductRequestDTO productRequestDTO) {
        productService.updateProduct(id, productRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }

}
