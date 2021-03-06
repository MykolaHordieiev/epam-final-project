package org.test.project.product;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.getProductById(id).orElseThrow(() -> new ProductException("product with id: "
                + id + " doesn't exist"));
    }

    public List<Product> getAllProduct() {
        return productRepository.getAllProducts();
    }
}
