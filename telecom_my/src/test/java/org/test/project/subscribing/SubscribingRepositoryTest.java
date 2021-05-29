package org.test.project.subscribing;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.Subscriber;

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
    private static final String GET_SUBSCRIBING = "SELECT * FROM subscribing WHERE subscriber_id=1";
    private static final String GET_PRODUCT = "SELECT * FROM product WHERE id=1";
    private static final String GET_RATE = "SELECT * FROM rate WHERE id=1";
    private static final Long ID = 1L;

    @SneakyThrows
    @Before
    public void setUp() {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @SneakyThrows
    @Test
    public void addSubscribingWhenTransactionIsCommit() {
        Subscriber subscriber = new Subscriber(ID, "log", "pass", 0, false);
        Product product = new Product(ID, "product");
        Rate rate = new Rate(ID, "rate", 10d, ID, false);
        Subscribing subscribing = new Subscribing(subscriber, product, rate);

        when(connection.prepareStatement(ADD_SUBSCRIBING)).thenReturn(preparedStatement);
        when(connection.prepareStatement(WITHDRAWN)).thenReturn(preparedStatement1);

        Subscriber resultSubscriber = repository.addSubscribing(subscribing);
        assertNotNull(resultSubscriber);
        assertEquals(subscriber, resultSubscriber);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1, ID);
        verify(preparedStatement).setLong(2, ID);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(preparedStatement1).setDouble(1, subscriber.getBalance());
        verify(preparedStatement1).execute();
        verify(connection).commit();
    }

    @SneakyThrows
    @Test(expected = SubscribingException.class)
    public void addSubscribingWhenTransactionIsRollback() {
        Subscriber subscriber = new Subscriber(ID, "log", "pass", 0, false);
        Product product = new Product(ID, "product");
        Rate rate = new Rate(ID, "rate", 10d, ID, false);
        Subscribing subscribing = new Subscribing(subscriber, product, rate);

        when(connection.prepareStatement(ADD_SUBSCRIBING)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(SQLException.class);
        repository.addSubscribing(subscribing);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1, ID);
        verify(preparedStatement).setLong(2, ID);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(connection).rollback();
    }

    @SneakyThrows
    @Test
    public void getSubscribingBySubscriberIdWhenFound() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Product product = new Product();
        product.setId(ID);
        Product product1 = new Product();
        product1.setId(2L);
        Rate rate = new Rate();
        rate.setId(ID);
        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        Subscribing subscribing1 = new Subscribing(subscriber, product1, rate);
        List<Subscribing> subscribingList = Arrays.asList(subscribing, subscribing1);

        when(statement.executeQuery(GET_SUBSCRIBING)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("product_id")).thenReturn(ID).thenReturn(2L);
        when(resultSet.getLong("rate_id")).thenReturn(ID).thenReturn(ID);


        List<Subscribing> resultList = repository.getSubscribingBySubscriberId(subscriber);
        assertNotNull(resultList);
        assertEquals(subscribingList, resultList);

        verify(resultSet).next();
        verify(resultSet).getLong("product_id");
        verify(resultSet).getLong("rate_id");
    }

    @SneakyThrows
    @Test
    public void getSubscribingBySubscriberIdWhenNotFound() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        List<Subscribing> subscribingList = Collections.emptyList();

        when(statement.executeQuery(GET_SUBSCRIBING)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Subscribing> resultList = repository.getSubscribingBySubscriberId(subscriber);
        assertNotNull(resultList);
        assertEquals(subscribingList, resultList);

        verify(dataSource).getConnection();
        verify(connection).createStatement();
        verify(statement).executeQuery(GET_SUBSCRIBING);
        verify(resultSet).next();
    }

    @SneakyThrows
    @Test
    public void getProductWhenFound() {
        Product product = new Product();
        product.setId(ID);
        Product expectedProduct = new Product(ID, "product");
        when(statement.executeQuery(GET_PRODUCT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name_product")).thenReturn("product");

        Product resultProduct = repository.getProduct(product);
        assertNotNull(resultProduct);
        assertEquals(expectedProduct, resultProduct);

        verify(resultSet).next();
        verify(resultSet).getString("name_product");
    }

    @SneakyThrows
    @Test
    public void getProductWhenNotFound() {
        Product product = new Product();
        product.setId(ID);

        when(statement.executeQuery(GET_PRODUCT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Product resultProduct = repository.getProduct(product);
        assertNotNull(resultProduct);
        assertEquals(product, resultProduct);

        verify(dataSource).getConnection();
        verify(connection).createStatement();
        verify(statement).executeQuery(GET_PRODUCT);
        verify(resultSet).next();
    }

    @SneakyThrows
    @Test
    public void getRateByWhenRateFound() {
        Rate rate = new Rate();
        rate.setId(ID);
        Rate expectedRate = new Rate(ID, "rate", 10d, ID, false);

        when(statement.executeQuery(GET_RATE)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name_rate")).thenReturn("rate");
        when(resultSet.getDouble("price")).thenReturn(10d);
        when(resultSet.getLong("product_id")).thenReturn(ID);
        when(resultSet.getBoolean("unusable")).thenReturn(false);

        Rate resultRate = repository.getRateBy(rate);
        assertNotNull(resultRate);
        assertEquals(expectedRate, resultRate);

        verify(resultSet).getLong("product_id");
        verify(resultSet).getString("name_rate");
        verify(resultSet).getDouble("price");
        verify(resultSet).getBoolean("unusable");
    }

    @SneakyThrows
    @Test
    public void getRateByWhenRateNotFound() {
        Rate rate = new Rate();
        rate.setId(ID);

        when(statement.executeQuery(GET_RATE)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Rate resultRate = repository.getRateBy(rate);
        assertNotNull(resultRate);
        assertEquals(rate, resultRate);

        verify(dataSource).getConnection();
        verify(connection).createStatement();
        verify(statement).executeQuery(GET_RATE);
        verify(resultSet).next();
    }
}