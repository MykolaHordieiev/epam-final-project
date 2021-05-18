package org.test.project.product;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.rate.Rate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public List<Product> getAllProducts() {
        String getAllProducts = "SELECT * FROM product ORDER BY id";
        List<Product> listOfProducts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllProducts)) {
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name_product"));
                listOfProducts.add(product);
            }
            return listOfProducts;
        }
    }
    @SneakyThrows
    public List<Rate> getAllRatesOfProduct() {
        List<Rate> listOfRate = new ArrayList<>();
        String getAllRates = "SELECT * FROM rate ORDER BY product_id";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllRates)) {
            while (resultSet.next()) {
                Rate rate = new Rate();
                rate.setId(resultSet.getLong("id"));
                rate.setName(resultSet.getString("name_rate"));
                rate.setPrice(resultSet.getDouble("price"));
                rate.setProductId(resultSet.getLong("product_id"));
                listOfRate.add(rate);
            }
            return listOfRate;
        }
    }

    @SneakyThrows
    public Optional<Product> getProductById(Long id) {
        String query = "SELECT * FROM product WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name_product"));
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }
}
