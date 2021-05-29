package org.test.project.rate;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateRepositoryTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private Statement statement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private RateRepository repository;

    private static final String GET_ALL_RATES = "SELECT * FROM rate WHERE product_id=1";
    private static final String GET_RATE_BY_ID = "SELECT * FROM rate WHERE id=1";
    private static final String CHANGE_RATE = "UPDATE rate SET name_rate=?, price=? WHERE id=?";
    private static final String INSERT_RATE = "INSERT INTO rate (name_rate, price, product_id) VALUES (?,?,?)";
    private static final String DELETE_RATE = "DELETE FROM rate WHERE id=1";
    private static final String GET_SUBSCRIBER = "SELECT subscriber_id FROM subscribing WHERE rate_id=1";
    private static final String DO_UNUSABLE_RATE = "UPDATE rate SET unusable=true WHERE id=1";
    private static final Long ID = 1L;
    private static final String NAME = "super";
    private static final Double PRICE = 40d;
//    private static final Long ID = 1L;

    @SneakyThrows
    @Before
    public void setDataSource() {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @SneakyThrows
    @Test
    public void getRatesByProductWhenRateFound() {
        Rate rate1 = new Rate(ID, NAME, PRICE, ID, false);
        Rate rate2 = new Rate(2L, "low", 20d, ID, true);
        List<Rate> expectedList = Arrays.asList(rate1, rate2);

        when(statement.executeQuery(GET_ALL_RATES)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(ID).thenReturn(2L);
        when(resultSet.getString("name_rate")).thenReturn(NAME).thenReturn("low");
        when(resultSet.getDouble("price")).thenReturn(PRICE).thenReturn(20d);
        when(resultSet.getBoolean("unusable")).thenReturn(false).thenReturn(true);

        List<Rate> resultList = repository.getRatesByProduct(ID);
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
    }

    @SneakyThrows
    @Test
    public void getRatesByProductWhenRateNotFound() {
        List<Rate> expectedList = Collections.emptyList();

        when(statement.executeQuery(GET_ALL_RATES)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Rate> resultList = repository.getRatesByProduct(ID);
        assertTrue(resultList.isEmpty());
        assertEquals(expectedList, resultList);
    }

    @SneakyThrows
    @Test
    public void getRateByIdWhenRateFound() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);

        when(statement.executeQuery(GET_RATE_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("product_id")).thenReturn(ID);
        when(resultSet.getString("name_rate")).thenReturn(NAME);
        when(resultSet.getDouble("price")).thenReturn(PRICE);
        when(resultSet.getBoolean("unusable")).thenReturn(false);

        Optional<Rate> resultRate = repository.getRateById(ID);
        assertNotNull(resultRate);
        assertTrue(resultRate.isPresent());
        assertEquals(rate, resultRate.get());
    }

    @SneakyThrows
    @Test
    public void getRateByIdWhenRateNotFound() {
        when(statement.executeQuery(GET_RATE_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Rate> resultRate = repository.getRateById(ID);
        assertFalse(resultRate.isPresent());
    }

    @SneakyThrows
    @Test
    public void changeRateById() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);
        when(connection.prepareStatement(CHANGE_RATE)).thenReturn(preparedStatement);

        Rate resultRate = repository.changeRateById(rate);
        assertNotNull(resultRate);
        assertEquals(rate, resultRate);

        verify(preparedStatement).setString(1, NAME);
        verify(preparedStatement).setDouble(2, PRICE);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
    }

    @SneakyThrows
    @Test
    public void addRateByProductId() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);
        when(connection.prepareStatement(INSERT_RATE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);

        Optional<Rate> resultRate = repository.addRateByProductId(rate);
        assertTrue(resultRate.isPresent());
        assertEquals(rate, resultRate.get());

        verify(preparedStatement).setString(1, NAME);
        verify(preparedStatement).setDouble(2, PRICE);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
        verify(resultSet).next();
    }

    @SneakyThrows
    @Test
    public void deleteRateById() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);

        Rate resultRate = repository.deleteRateById(rate);
        assertEquals(rate, resultRate);

        verify(statement).execute(DELETE_RATE);
    }

    @SneakyThrows
    @Test
    public void checkUsingRateBySubscribersWhenFound() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(ID);
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        List<Subscriber> expected = Arrays.asList(subscriber1, subscriber2);

        when(statement.executeQuery(GET_SUBSCRIBER)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("subscriber_id")).thenReturn(ID).thenReturn(2L);

        List<Subscriber> resultList = repository.checkUsingRateBySubscribers(rate);
        assertEquals(expected, resultList);
    }

    @SneakyThrows
    @Test
    public void checkUsingRateBySubscribersWhenNotFound() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);
        List<Subscriber> expected = Collections.emptyList();
        when(statement.executeQuery(GET_SUBSCRIBER)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        List<Subscriber> resultList = repository.checkUsingRateBySubscribers(rate);
        assertEquals(expected, resultList);
        assertTrue(resultList.isEmpty());
    }

    @SneakyThrows
    @Test
    public void doUnusableRateByRateId() {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);

        Rate resultRate = repository.doUnusableRateByRateId(rate);
        assertEquals(rate, resultRate);

        verify(statement).execute(DO_UNUSABLE_RATE);
    }
}