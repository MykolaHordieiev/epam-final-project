package org.test.project.operator;

import liquibase.pro.packaged.E;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.entity.Entity;
import org.test.project.entity.Product;
import org.test.project.entity.Rate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OperatorRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public boolean addRateByProductId(String nameOfRate, double price, Long idofProduct) {
        String addRate = "INSERT INTO rate (name_rate, price, product_id) VALUES (?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addRate)) {
            preparedStatement.setString(1, nameOfRate);
            preparedStatement.setDouble(2, price);
            preparedStatement.setLong(3, idofProduct);
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public boolean deleteRateByProductId(Long id) {
        String query = "DELETE FROM rate WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.execute(query);
        }
    }

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
                rate.setProduct_id(resultSet.getLong("product_id"));
                listOfRate.add(rate);
            }
            return listOfRate;
        }
    }

    @SneakyThrows
    public Map<Product, Rate> getProductsAndRates() {
        Map<Product, Rate> mapOfProductAndRate = new Hashtable<>();
        String getProductsAndRates = "SELECT * FROM product JOIN rate ON product.id=rate.product_id ORDER BY product_id";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getProductsAndRates)) {
            while (resultSet.next()) {
                Product product = new Product();
                Rate rate = new Rate();
                product.setId(resultSet.getLong(1));
                product.setName(resultSet.getString("name_product"));
                rate.setId(resultSet.getLong(3));
                rate.setName(resultSet.getString("name_rate"));
                rate.setPrice(resultSet.getDouble("price"));
                mapOfProductAndRate.put(product, rate);
            }
        }
        return mapOfProductAndRate;
    }

    @SneakyThrows
    public boolean changeRate(Long id, String name, Double price) {
        String query;
        if (name.equals("")) {
            query = "UPDATE rate SET price=" + price + " WHERE id=" + id;
        } else if (price.equals(0.0)) {
            query = "UPDATE rate SET name_rate='" + name + "' WHERE id=" + id;
        } else {
            query = "UPDATE rate SET name_rate='" + name + "',price=" + price + " WHERE id=" + id;
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.execute();
        }
    }


}

