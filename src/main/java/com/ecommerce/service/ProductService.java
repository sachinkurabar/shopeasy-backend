package com.ecommerce.service;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // CREATE PRODUCT
    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        ));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    // GET ALL PRODUCTS
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET PRODUCT BY ID
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        ));

        return convertToResponse(product);
    }

    // UPDATE PRODUCT
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        ));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        ));

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setStock(request.getStock());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);

        return convertToResponse(updatedProduct);
    }

    // DELETE PRODUCT
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(
                    "Product not found with id: " + id
            );
        }

        productRepository.deleteById(id);
    }

    // CONVERT ENTITY TO RESPONSE DTO
    private ProductResponse convertToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}