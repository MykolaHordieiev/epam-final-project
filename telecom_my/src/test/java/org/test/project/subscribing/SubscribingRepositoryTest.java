package org.test.project.subscribing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscribingRepositoryTest {

    @Mock
    private DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    PreparedStatement preparedStatement1;
    @Mock
    Statement statement;

    @InjectMocks
    private SubscribingRepository repository;

    private static final String ADD_SUBSCRIBING = "INSERT INTO subscribing VALUES(?,?,?)";
    private static final String WITHDRAWN = "UPDATE subscriber SET balance=? WHERE id=1";
    private static final String GET_SUBSCRIBING = "SELECT user.login, subscriber.balance, subscriber.locked, " +
            "subscribing.product_id, name_product, " +
            "rate_id, name_rate, rate.price, rate.unusable FROM subscribing " +
            "INNER JOIN user ON user.id = subscribing.subscriber_id " +
            "INNER JOIN subscriber ON subscriber.id = subscribing.subscriber_id " +
            "INNER JOIN rate ON rate.id = subscribing.rate_id " +
            "INNER JOIN product on product.id=subscribing.product_id WHERE subscriber_id=1";
    private static final Long ID = 1L;
    private static SubscriberAddSubscribingDTO subscriberDTO = new SubscriberAddSubscribingDTO(ID, 10d);

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    public void addSubscribingWhenTransactionIsCommit() throws SQLException {
        when(connection.prepareStatement(ADD_SUBSCRIBING)).thenReturn(preparedStatement);
        when(connection.prepareStatement(WITHDRAWN)).thenReturn(preparedStatement1);

        SubscriberAddSubscribingDTO resultSubscriber = repository.addSubscribing(subscriberDTO, ID, ID);
        assertNotNull(resultSubscriber);
        assertEquals(subscriberDTO, resultSubscriber);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1, ID);
        verify(preparedStatement).setLong(2, ID);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(preparedStatement1).setDouble(1, subscriberDTO.getBalance());
        verify(preparedStatement1).execute();
        verify(connection).commit();
    }

    @Test(expected = SubscribingException.class)
    public void addSubscribingWhenTransactionIsRollback() throws SQLException {
        when(connection.prepareStatement(ADD_SUBSCRIBING)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(SQLException.class);
        repository.addSubscribing(subscriberDTO, ID, ID);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1, ID);
        verify(preparedStatement).setLong(2, ID);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(connection).rollback();
    }

    @Test
    public void getSubscribingBySubscriberIdWhenFound() throws SQLException {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        subscriber.setLogin("logg");
        subscriber.setBalance(20d);
        subscriber.setLock(false);

        Product product = new Product(ID, "mobile");
        Product product1 = new Product(2L, "internet");

        Rate rate = new Rate(ID, "low", 10d, ID, false);
        Rate rate1 = new Rate(3L, "high", 20d, 2L, false);

        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        Subscribing subscribing1 = new Subscribing(subscriber, product1, rate1);
        List<Subscribing> subscribingList = Arrays.asList(subscribing, subscribing1);

        when(statement.executeQuery(GET_SUBSCRIBING)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("login")).thenReturn("logg");
        when(resultSet.getDouble("balance")).thenReturn(20d);
        when(resultSet.getBoolean("locked")).thenReturn(false);

        when(resultSet.getLong("product_id")).thenReturn(ID).thenReturn(ID).thenReturn(2L).thenReturn(2L);
        when(resultSet.getString("name_product")).thenReturn("mobile").thenReturn("internet");

        when(resultSet.getLong("rate_id")).thenReturn(ID).thenReturn(3L);
        when(resultSet.getString("name_rate")).thenReturn("low").thenReturn("high");
        when(resultSet.getDouble("price")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getBoolean("unusable")).thenReturn(false);

        List<Subscribing> resultList = repository.getSubscribingBySubscriberId(ID);
        assertNotNull(resultList);
        assertEquals(subscribingList, resultList);
    }

    @Test
    public void getSubscribingBySubscriberIdWhenNotFound() throws SQLException {
        List<Subscribing> subscribingList = Collections.emptyList();

        when(statement.executeQuery(GET_SUBSCRIBING)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Subscribing> resultList = repository.getSubscribingBySubscriberId(ID);
        assertNotNull(resultList);
        assertEquals(subscribingList, resultList);
    }
}