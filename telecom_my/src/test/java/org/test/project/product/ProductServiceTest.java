package org.test.project.product;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static final Long ID = 2L;

    @Test
    public void getProductByIdWhenRepositoryReturnProduct() {
        Product product = new Product();
        product.setId(ID);
        when(productRepository.getProductById(ID)).thenReturn(Optional.of(product));

        Product resultProduct = productService.getProductById(ID);
        Assert.assertEquals(product, resultProduct);
    }

    @Test(expected = ProductException.class)
    public void getProductByIdWhenRepositoryReturnEmptyOptional() {
        when(productRepository.getProductById(ID)).thenReturn(Optional.empty());

        productService.getProductById(ID);
    }

    @Test
    public void getAllProduct() {
        List<Product> expectedProductList = Arrays.asList(new Product(), new Product());
        when(productRepository.getAllProducts()).thenReturn(expectedProductList);

        List<Product> resultProductList = productService.getAllProduct();
        Assert.assertEquals(expectedProductList, resultProductList);
    }
}