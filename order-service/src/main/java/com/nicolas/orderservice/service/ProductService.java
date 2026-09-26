package com.nicolas.orderservice.service;

import com.nicolas.orderservice.dto.request.ProductRequestDTO;
import com.nicolas.orderservice.dto.response.ProductResponseDTO;
import com.nicolas.orderservice.entity.ProductEntity;
import com.nicolas.orderservice.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final IProductRepository productRepository;

    public void createProduct(ProductRequestDTO productRequestDTO) {
        if (productRepository.existsByName(productRequestDTO.name())) {
            throw new IllegalArgumentException("A product with this name already exists");
        }

        ProductEntity newProduct = ProductEntity.builder()
                .name(productRequestDTO.name())
                .price(productRequestDTO.price())
                .stock(productRequestDTO.stock()).build();

        productRepository.save(newProduct);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream().map(p ->
                new ProductResponseDTO(p.getId(), p.getName(), p.getPrice(), p.getStock())
        ).toList();
    }

    public void updateProduct(UUID id, ProductRequestDTO requestDTO) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!existingProduct.getName().equals(requestDTO.name()) && productRepository.existsByName(requestDTO.name())) {
            throw new IllegalArgumentException("A product with this name already exists");
        }

        existingProduct.setName(requestDTO.name());
        existingProduct.setPrice(requestDTO.price());
        existingProduct.setStock(requestDTO.stock());

        productRepository.save(existingProduct);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }


}
