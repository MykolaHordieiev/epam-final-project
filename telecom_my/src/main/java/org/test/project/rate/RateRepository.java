package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.test.project.rate.dto.RateAddRequestDTO;
import org.test.project.rate.dto.RateChangeRequestDTO;
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
                rates.add(new Rate(id, name, prise, productId, unusable));
            }
        }
        return rates;
    }

    @SneakyThrows
    public List<Rate> getRatesBySubscriberId(Long subscriberId) {
        String query = "SELECT * FROM rate JOIN subscribing ON subscribing.rate_id=rate.id WHERE subscriber_id =" + subscriberId;
        List<Rate> rates = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name_rate");
                double prise = resultSet.getDouble("price");
                boolean unusable = resultSet.getBoolean("unusable");
                long productId = resultSet.getLong("rate.product_id");
                rates.add(new Rate(id, name, prise, productId, unusable));
            }
        }
        return rates;
    }

    @SneakyThrows
    public Optional<Rate> getRateById(Long id) {
        String query = "SELECT * FROM rate WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                Rate rate = new Rate();
                rate.setId(id);
                rate.setProductId(resultSet.getLong("product_id"));
                rate.setName(resultSet.getString("name_rate"));
                rate.setPrice(resultSet.getDouble("price"));
                rate.setUnusable(resultSet.getBoolean("unusable"));
                return Optional.of(rate);
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    public RateChangeRequestDTO changeRateById(RateChangeRequestDTO rateDTO) {
        String query = "UPDATE rate SET name_rate=?, price=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, rateDTO.getRateName());
            preparedStatement.setDouble(2, rateDTO.getPrice());
            preparedStatement.setLong(3, rateDTO.getRateId());
            preparedStatement.execute();
        }
        return rateDTO;
    }

    @SneakyThrows
    public RateAddRequestDTO addRateByProductId(RateAddRequestDTO rateAddRequestDTO) {
        String addRate = "INSERT INTO rate (name_rate, price, product_id) VALUES (?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addRate, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, rateAddRequestDTO.getRateName());
            preparedStatement.setDouble(2, rateAddRequestDTO.getPrice());
            preparedStatement.setLong(3, rateAddRequestDTO.getProductId());
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                rateAddRequestDTO.setRateId(resultSet.getLong(1));
            }
        }
        return rateAddRequestDTO;
    }

    @SneakyThrows
    public Long deleteRateById(Long id) {
        String deleteQuery = "DELETE FROM rate WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteQuery);
        }
        return id;
    }

    @SneakyThrows
    public List<Subscriber> checkUsingRateBySubscribers(Long id) {
        String query = "SELECT id, login FROM subscribing JOIN user ON subscribing.subscriber_id=user.id  WHERE rate_id=" + id;
        List<Subscriber> subscriberList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(resultSet.getLong("id"));
                subscriber.setLogin(resultSet.getString("login"));
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
        }
        rate.setUnusable(true);
        return rate;
    }
}
