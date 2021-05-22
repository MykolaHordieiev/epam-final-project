package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.FiledTransactionException;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SubscribingRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public Subscriber addSubscribing(Subscribing subscribing) {
        String addSubscribing = "INSERT INTO subscribing VALUES(?,?,?)";
        String withdrawn = "UPDATE subscriber SET balance=? WHERE id=" + subscribing.getSubscriber().getId();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(addSubscribing);
            preparedStatement.setDouble(1, subscribing.getSubscriber().getId());
            preparedStatement.setDouble(2, subscribing.getProduct().getId());
            preparedStatement.setDouble(3, subscribing.getRate().getId());
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(withdrawn)) {
                preparedStatement1.setDouble(1, subscribing.getSubscriber().getBalance());
                preparedStatement1.execute();
                connection.commit();
            }
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw new FiledTransactionException("filed transaction in addSubscribing");
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return subscribing.getSubscriber();
    }

    @SneakyThrows
    public List<Subscribing> getSubscribingBySubscriberId(Long id) {
        String getSubscribing = "SELECT * FROM subscribing WHERE subscriber_id=" + id;
        List<Subscribing> listOfSubscribing = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getSubscribing)) {
            while (resultSet.next()) {
                Product product = getProduct(resultSet.getLong("product_id"));
                Rate rate = getRateBy(resultSet.getLong("rate_id"));
                Subscribing subscribing = new Subscribing();
                subscribing.setProduct(product);
                subscribing.setRate(rate);
                listOfSubscribing.add(subscribing);
            }
        }
        return listOfSubscribing;
    }

    @SneakyThrows
    private Product getProduct(Long productId) {
        String query = "SELECT * FROM product WHERE id=" + productId;
        Product product = new Product();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name_product"));
            }
        }
        return product;
    }

    @SneakyThrows
    private Rate getRateBy(Long rateId) {
        String query = "SELECT * FROM rate WHERE id=" + rateId;
        Rate rate = new Rate();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                rate.setProductId(resultSet.getLong("product_id"));
                rate.setName(resultSet.getString("name_rate"));
                rate.setPrice(resultSet.getDouble("price"));
            }
        }
        return rate;
    }


    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}