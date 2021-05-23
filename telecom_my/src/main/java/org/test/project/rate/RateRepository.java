package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RateRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public List<Rate> getRatesByProduct(Long productId) {
        String query = "SELECT * FROM rate WHERE product_id=" + productId;
        List<Rate> rates = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name_rate");
                double prise = resultSet.getDouble("price");
                boolean unusable = resultSet.getBoolean("unusable");
                rates.add(new Rate(id, name, prise, productId,unusable));
            }
        }
        return rates;
    }

    @SneakyThrows
    public Optional<Rate> getRateById(long id) {
        String query = "SELECT * FROM rate WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("name_rate");
                double prise = resultSet.getDouble("price");
                boolean unusable = resultSet.getBoolean("unusable");
                return Optional.of(new Rate(id, name, prise, productId,unusable));
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    public Rate changeRateById(Rate rate) {
        String query = "UPDATE rate SET name_rate='" + rate.getName() + "',price=" + rate.getPrice()
                + " WHERE id=" + rate.getId();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
            return rate;
        }
    }

    @SneakyThrows
    public Optional<Rate> addRateByProductId(Rate rate) {
        String addRate = "INSERT INTO rate (name_rate, price, product_id) VALUES (?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addRate, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, rate.getName());
            preparedStatement.setDouble(2, rate.getPrice());
            preparedStatement.setLong(3, rate.getProductId());
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                rate.setId(resultSet.getLong(1));
                return Optional.of(rate);
            }
        }
    }

    @SneakyThrows
    public Rate deleteRateById(Rate rate) {
        String deleteQuery = "DELETE FROM rate WHERE id=" + rate.getId();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteQuery);
        }
        return rate;
    }

    @SneakyThrows
    public List<Subscriber> checkUsingRateBySubscribers(Rate rate) {
        String query = "SELECT subscriber_id FROM subscribing WHERE rate_id=" + rate.getId();
        List<Subscriber> subscriberList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(resultSet.getLong("subscriber_id"));
                subscriberList.add(subscriber);
            }
        }
        return subscriberList;
    }

    @SneakyThrows
    public Rate doUnusableRateByRateId(Rate rate) {
        String query = "UPDATE rate SET unusable=true WHERE id=" + rate.getId();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
            rate.setUnusable(true);
        }
        return rate;
    }
}
