package com.infy.backend.service;

import java.util.List;

import com.infy.backend.io.ProductRequest;
import com.infy.backend.io.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getActiveProducts();

    ProductResponse updateProduct(String productId, ProductRequest request);

    ProductResponse getProductById(String productId);

    void deleteProduct(String productId);
}