package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;

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
            if (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setLogin(resultSet.getString("login"));
                subscriber.setBalance(resultSet.getDouble("balance"));
                subscriber.setLock(resultSet.getBoolean("locked"));
                subscriber.setId(id);
                return Optional.of(subscriber);
            }
            return Optional.empty();
        }
    }

    @SneakyThrows
    public Optional<Subscriber> getByLogin(String login) {
        String query = "SELECT user.id, balance, locked FROM user JOIN subscriber ON user.id=subscriber.id " +
                "WHERE user.login='" + login + "'";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(resultSet.getLong("id"));
                subscriber.setLogin(login);
                subscriber.setBalance(resultSet.getDouble("balance"));
                subscriber.setLock(resultSet.getBoolean("locked"));
                return Optional.of(subscriber);
            }
            return Optional.empty();
        }
    }

    @SneakyThrows
    public SubscriberCreateDTO insertSubscriber(SubscriberCreateDTO subscriberCreateDTO) {
        String insertIntoUser = "INSERT INTO user (login,password,role) VALUES (?,?,?);";
        String insertIntoSubscriber = "INSERT INTO subscriber (id) VALUES (?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertIntoUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, subscriberCreateDTO.getLogin());
            preparedStatement.setString(2, subscriberCreateDTO.getPassword());
            preparedStatement.setString(3, subscriberCreateDTO.getUserRole().toString());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                long id = generatedKeys.getLong(1);
                subscriberCreateDTO.setId(id);
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
        return subscriberCreateDTO;
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
    public Subscriber lockSubById(Long id) {
        String query = "UPDATE subscriber SET locked=true WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        subscriber.setLock(true);
        return subscriber;
    }

    @SneakyThrows
    public Subscriber unlockSubById(Long id) {
        String query = "UPDATE subscriber SET locked=false WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        subscriber.setLock(false);
        return subscriber;
    }

    @SneakyThrows
    public SubscriberReplenishDTO replenishBalanceById(SubscriberReplenishDTO replenishDTO) {
        String query = "UPDATE subscriber SET balance=? WHERE id=" + replenishDTO.getId();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, replenishDTO.getBalance());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new SubscriberException("filed top up balance");
        }
        return replenishDTO;
    }

    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}