package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscribing.Subscribing;

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
    public Optional<Subscriber> getById(Subscriber subscriber) {
        String query = "SELECT * FROM user JOIN subscriber ON user.id=subscriber.id WHERE user.id=" + subscriber.getId();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                subscriber.setLogin(resultSet.getString("login"));
                subscriber.setBalance(resultSet.getDouble("balance"));
                subscriber.setLock(resultSet.getBoolean("locked"));
                return Optional.of(subscriber);
            }
            return Optional.empty();
        }
    }

    @SneakyThrows
    public Optional<Subscriber> getByLogin(Subscriber subscriber) {
        String query = "SELECT id FROM user WHERE user.login='" + subscriber.getLogin() + "'";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                subscriber.setId(resultSet.getLong("id"));
                return Optional.of(subscriber);
            }
            return Optional.empty();
        }
    }

    @SneakyThrows
    public Subscriber insertSubscriber(Subscriber subscriber) {
        String insertIntoUser = "INSERT INTO user (login,password,role) VALUES (?,?,?);";
        String insertIntoSubscriber = "INSERT INTO subscriber (id) VALUES (?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertIntoUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, subscriber.getLogin());
            preparedStatement.setString(2, subscriber.getPassword());
            preparedStatement.setString(3, subscriber.getUserRole().toString());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                long id = generatedKeys.getLong(1);
                subscriber.setId(id);
                try (PreparedStatement preparedStatement1 = connection.prepareStatement(insertIntoSubscriber)) {
                    preparedStatement1.setLong(1, id);
                    preparedStatement1.execute();
                    connection.commit();
                }
            }
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw new SubscriberException("transaction failed with create Subscriber");
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
    public Subscriber lockSubById(Subscriber subscriber) {
        String query = "UPDATE subscriber SET locked=true WHERE id=" + subscriber.getId();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
        subscriber.setLock(true);
        return subscriber;
    }

    @SneakyThrows
    public Subscriber unlockSubById(Subscriber subscriber) {
        String query = "UPDATE subscriber SET locked=false WHERE id=" + subscriber.getId();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
        subscriber.setLock(false);
        return subscriber;
    }

    @SneakyThrows
    public Subscriber topUpBalanceById(Subscriber subscriber, Double newBalance) {
        String query = "UPDATE subscriber SET balance=? WHERE id=" + subscriber.getId();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new SubscriberException("filed top up balance");
        }
        subscriber.setBalance(newBalance);
        return subscriber;
    }

    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}