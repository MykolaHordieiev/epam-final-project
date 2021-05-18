package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.test.project.entity.Product;
import org.test.project.rate.Rate;
import org.test.project.entity.Subscribing;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class SubscriberRepository {


    private final DataSource dataSource;


    @SneakyThrows
    public Optional<Subscriber> getById(Long id) {
        String query = "SELECT * FROM user JOIN subscriber ON user.id=subscriber.id WHERE user.id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(resultSet.getLong("id"));
                subscriber.setLogin(resultSet.getString("login"));
                subscriber.setBalance(resultSet.getDouble("balance"));
                subscriber.setLock(resultSet.getBoolean("locked"));
                return Optional.of(subscriber);
            }
            return Optional.empty();
        }

    }

    @SneakyThrows
    public Subscriber insertSubscriber(Subscriber subscriber) {
        String insertInToUser = "INSERT INTO user (login,password,role) VALUES (?,?,?);";
        String insertInToSubscriber = "INSERT INTO subscriber (id) VALUES (?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertInToUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, subscriber.getLogin());
            preparedStatement.setString(2, subscriber.getPassword());
            preparedStatement.setString(3, "SUBSCRIBER");
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                Long id = generatedKeys.getLong(1);
                subscriber.setId(id);
                try (PreparedStatement preparedStatement1 = connection.prepareStatement(insertInToSubscriber)) {
                    preparedStatement1.setLong(1, id);
                    preparedStatement1.execute();
                    connection.commit();
                }
            }
        } catch (Exception ex) {
            if (connection != null) {
                    connection.rollback();

            }
            System.out.println("throw fieldTransaction");
            throw new FiledTransactionException("transaction failed");
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return subscriber;
    }


    @SneakyThrows
    public List<Subscriber> getAll() {
        String query = "SELECT * FROM user JOIN subscriber ON user.id=subscriber.id";
        List<Subscriber> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(resultSet.getLong(1));
                subscriber.setLogin(resultSet.getString("login"));
                subscriber.setBalance(resultSet.getDouble("balance"));
                subscriber.setLock(resultSet.getBoolean("locked"));
                list.add(subscriber);
            }
            return list;
        }
    }

    @SneakyThrows
    public boolean lockSubById(Long id) {
        String query = "UPDATE subscriber SET locked=true WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.execute(query);
        }
    }

    @SneakyThrows
    public boolean unLockSubById(Long id) {
        String query = "UPDATE subscriber SET locked=false WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.execute(query);
        }
    }

    @SneakyThrows
    public boolean topUpBalanceById(Long id, Double amount) {
        String query = "UPDATE subscriber SET balance=? WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, amount);
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public Optional<Rate> getRate(Long id) {
        String query = "SELECT * FROM rate WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                Rate rate = new Rate();
                rate.setId(resultSet.getLong("id"));
                rate.setName(resultSet.getString("name_rate"));
                rate.setPrice(resultSet.getDouble("price"));
                rate.setProductId(resultSet.getLong("product_id"));
                return Optional.of(rate);
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    public Optional<Product> getProduct(Long id) {
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
                Product product = new Product();
                Rate rate = new Rate();
                product.setId(resultSet.getLong("product_id"));
                rate.setId(resultSet.getLong("rate_id"));
                Subscribing subscribing = new Subscribing();
                subscribing.setProduct(product);
                subscribing.setRate(rate);
                listOfSubscribing.add(subscribing);
            }
            for (int i = 0; i < listOfSubscribing.size(); i++) {
                System.out.println(listOfSubscribing.get(i).getProduct().getId());
                System.out.println(listOfSubscribing.get(i).getRate().getId());
            }
            System.out.println(listOfSubscribing);
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