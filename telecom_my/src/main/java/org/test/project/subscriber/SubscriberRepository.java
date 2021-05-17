package org.test.project.subscriber;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.test.project.entity.Product;
import org.test.project.entity.Rate;

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
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
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
                rate.setProduct_id(resultSet.getLong("product_id"));
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

    public void addSubscribing(Long idOfSubscriber, Long idOfProduct, Long idOfRate, Double balance) {
        String addSubscribing = "INSERT INTO subscribing VALUES(?,?,?)";
        String withdrawn = "UPDATE subscriber SET balance=? WHERE id=" + idOfSubscriber;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(addSubscribing);
            preparedStatement.setDouble(1, idOfSubscriber);
            preparedStatement.setDouble(2, idOfProduct);
            preparedStatement.setDouble(3, idOfRate);
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(withdrawn)) {
                preparedStatement1.setDouble(1, balance);
                preparedStatement1.execute();
                connection.commit();
            }
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
            throw new FiledTransactionException("filed transaction in addSubscribing");
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}