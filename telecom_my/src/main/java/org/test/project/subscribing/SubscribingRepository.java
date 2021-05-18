package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.test.project.subscriber.FiledTransactionException;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}
