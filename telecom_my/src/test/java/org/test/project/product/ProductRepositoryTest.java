package org.test.project.product;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private Statement statement;
    @Mock
    ResultSet resultSet;

    @InjectMocks
    private ProductRepository productRepository;

    private static final String GET_ALL_PRODUCTS = "SELECT * FROM product ORDER BY id";
    private static final String GET_PRODUCT = "SELECT * FROM product WHERE id=1";
    private static final Long ID = 1L;
    private static final String NAME = "internet";


    @SneakyThrows
    @Before
    public void setUp() {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @SneakyThrows
    @Test
    public void getAllProductsWhenProductFound() {
        Product product1 = new Product(ID, NAME);
        Product product2 = new Product(2L, "mobile");
        List<Product> expectedList = Arrays.asList(product1, product2);

        when(statement.executeQuery(GET_ALL_PRODUCTS)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(ID).thenReturn(2L);
        when(resultSet.getString("name_product")).thenReturn(NAME).thenReturn("mobile");

        List<Product> resultList = productRepository.getAllProducts();
        assertEquals(expectedList, resultList);

    }

    @SneakyThrows
    @Test
    public void getAllProductsWhenProductNotFound() {
        List<Product> expectedList = Collections.emptyList();

        when(statement.executeQuery(GET_ALL_PRODUCTS)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Product> resultList = productRepository.getAllProducts();
        assertEquals(expectedList, resultList);
    }

    @SneakyThrows
    @Test
    public void getProductByIdWhenFoundProduct() {
        Product product = new Product(ID, NAME);

        when(statement.executeQuery(GET_PRODUCT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getString("name_product")).thenReturn(NAME);

        Optional<Product> result = productRepository.getProductById(ID);
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @SneakyThrows
    @Test
    public void getProductByIdWhenNotFoundProduct() {
        when(statement.executeQuery(GET_PRODUCT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Product> result = productRepository.getProductById(ID);
        assertFalse(result.isPresent());
    }
}