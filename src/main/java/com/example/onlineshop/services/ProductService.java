package com.example.onlineshop.services;

import com.example.onlineshop.entitetes.Product;
import com.example.onlineshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }


    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public void deleteProductById(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
