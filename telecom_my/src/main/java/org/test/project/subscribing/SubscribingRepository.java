package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SubscribingRepository {

    private static Logger logger = LogManager.getLogger(SubscribingRepository.class);

    private final DataSource dataSource;

    @SneakyThrows
    public SubscriberAddSubscribingDTO addSubscribing(SubscriberAddSubscribingDTO subscriberDTO, Long productId, Long rateId) {
        String addSubscribing = "INSERT INTO subscribing VALUES(?,?,?)";
        String withdrawn = "UPDATE subscriber SET balance=? WHERE id=" + subscriberDTO.getId();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(addSubscribing);
            preparedStatement.setLong(1, subscriberDTO.getId());
            preparedStatement.setLong(2, productId);
            preparedStatement.setLong(3, rateId);
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(withdrawn)) {
                preparedStatement1.setDouble(1, subscriberDTO.getBalance());
                preparedStatement1.execute();
                connection.commit();
            }
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(ex.getMessage());
            throw new RuntimeException("filed transaction in addSubscribing");
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return subscriberDTO;
    }

    @SneakyThrows
    public List<Subscribing> getSubscribingBySubscriberId(Long id) {
        String getSubscribing = "SELECT user.login, subscriber.balance, subscriber.locked, " +
                "subscribing.product_id, name_product, " +
                "rate_id, name_rate, rate.price, rate.unusable FROM subscribing " +
                "INNER JOIN user ON user.id = subscribing.subscriber_id " +
                "INNER JOIN subscriber ON subscriber.id = subscribing.subscriber_id " +
                "INNER JOIN rate ON rate.id = subscribing.rate_id " +
                "INNER JOIN product on product.id=subscribing.product_id WHERE subscriber_id=" + id;
        List<Subscribing> listOfSubscribing = new ArrayList<>();
        Subscriber subscriber = new Subscriber();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getSubscribing)) {
            while (resultSet.next()) {
                subscriber.setId(id);
                subscriber.setLogin(resultSet.getString("login"));
                subscriber.setBalance(resultSet.getDouble("balance"));
                subscriber.setLock(resultSet.getBoolean("locked"));

                Product product = new Product();
                product.setId(resultSet.getLong("product_id"));
                product.setName(resultSet.getString("name_product"));

                Rate rate = new Rate();
                rate.setId(resultSet.getLong("rate_id"));
                rate.setName(resultSet.getString("name_rate"));
                rate.setPrice(resultSet.getDouble("price"));
                rate.setProductId(resultSet.getLong("product_id"));
                rate.setUnusable(resultSet.getBoolean("unusable"));

                Subscribing subscribing = new Subscribing();
                subscribing.setSubscriber(subscriber);
                subscribing.setProduct(product);
                subscribing.setRate(rate);
                listOfSubscribing.add(subscribing);
            }
        }
        return listOfSubscribing;
    }

    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}
