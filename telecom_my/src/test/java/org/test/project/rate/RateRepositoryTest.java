package org.test.project.rate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.rate.dto.RateAddRequestDTO;
import org.test.project.rate.dto.RateChangeRequestDTO;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;

import java.sql.*;
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

    private static final String GET_ALL_RATES_BY_PRODUCT_ID = "SELECT * FROM rate WHERE product_id=1";
    private static final String GET_ALL_RATES_BY_SUBSCRIBER_ID = "SELECT * FROM rate JOIN subscribing ON subscribing.rate_id=rate.id WHERE subscriber_id =1";
    private static final String GET_RATE_BY_ID = "SELECT * FROM rate WHERE id=1";
    private static final String CHANGE_RATE = "UPDATE rate SET name_rate=?, price=? WHERE id=?";
    private static final String INSERT_RATE = "INSERT INTO rate (name_rate, price, product_id) VALUES (?,?,?)";
    private static final String DELETE_RATE = "DELETE FROM rate WHERE id=1";
    private static final String GET_SUBSCRIBER = "SELECT id, login FROM subscribing JOIN user ON subscribing.subscriber_id=user.id  WHERE rate_id=1";
    private static final String DO_UNUSABLE_RATE = "UPDATE rate SET unusable=true WHERE id=1";
    private static final Long ID = 1L;
    private static final String NAME = "super";
    private static final Double PRICE = 40d;

    @Before
    public void setDataSource() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    public void getRatesByProductWhenRateFound() throws SQLException {
        Rate rate1 = new Rate(ID, NAME, PRICE, ID, false);
        Rate rate2 = new Rate(2L, "low", 20d, ID, true);
        List<Rate> expectedList = Arrays.asList(rate1, rate2);

        when(statement.executeQuery(GET_ALL_RATES_BY_PRODUCT_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(ID).thenReturn(2L);
        when(resultSet.getString("name_rate")).thenReturn(NAME).thenReturn("low");
        when(resultSet.getDouble("price")).thenReturn(PRICE).thenReturn(20d);
        when(resultSet.getBoolean("unusable")).thenReturn(false).thenReturn(true);

        List<Rate> resultList = repository.getRatesByProduct(ID);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getRatesByProductWhenRateNotFound() throws SQLException {
        List<Rate> expectedList = Collections.emptyList();

        when(statement.executeQuery(GET_ALL_RATES_BY_PRODUCT_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Rate> resultList = repository.getRatesByProduct(ID);
        assertTrue(resultList.isEmpty());
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getRatesBySubscriberIdWhenRateFound() throws SQLException {
        Rate rate1 = new Rate(ID, NAME, PRICE, ID, false);
        Rate rate2 = new Rate(2L, "low", 20d, ID, true);
        List<Rate> expectedList = Arrays.asList(rate1, rate2);

        when(statement.executeQuery(GET_ALL_RATES_BY_SUBSCRIBER_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(ID).thenReturn(2L);
        when(resultSet.getString("name_rate")).thenReturn(NAME).thenReturn("low");
        when(resultSet.getDouble("price")).thenReturn(PRICE).thenReturn(20d);
        when(resultSet.getBoolean("unusable")).thenReturn(false).thenReturn(true);
        when(resultSet.getLong("rate.product_id")).thenReturn(ID);

        List<Rate> resultList = repository.getRatesBySubscriberId(ID);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getRatesBySubscriberWhenRateNotFound() throws SQLException {
        List<Rate> expectedList = Collections.emptyList();

        when(statement.executeQuery(GET_ALL_RATES_BY_SUBSCRIBER_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Rate> resultList = repository.getRatesBySubscriberId(ID);
        assertTrue(resultList.isEmpty());
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getRateByIdWhenRateFound() throws SQLException {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);

        when(statement.executeQuery(GET_RATE_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("product_id")).thenReturn(ID);
        when(resultSet.getString("name_rate")).thenReturn(NAME);
        when(resultSet.getDouble("price")).thenReturn(PRICE);
        when(resultSet.getBoolean("unusable")).thenReturn(false);

        Optional<Rate> resultRate = repository.getRateById(ID);
        assertTrue(resultRate.isPresent());
        assertEquals(rate, resultRate.get());
    }

    @Test
    public void getRateByIdWhenRateNotFound() throws SQLException {
        when(statement.executeQuery(GET_RATE_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Rate> resultRate = repository.getRateById(ID);
        assertFalse(resultRate.isPresent());
    }

    @Test
    public void changeRateById() throws SQLException {
        RateChangeRequestDTO rateDTO = new RateChangeRequestDTO(ID, NAME, PRICE);

        when(connection.prepareStatement(CHANGE_RATE)).thenReturn(preparedStatement);

        RateChangeRequestDTO resultRateDTO = repository.changeRateById(rateDTO);
        assertEquals(rateDTO, resultRateDTO);

        verify(preparedStatement).setString(1, NAME);
        verify(preparedStatement).setDouble(2, PRICE);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
    }

    @Test
    public void addRateByProductId() throws SQLException {
        RateAddRequestDTO rateDTO = new RateAddRequestDTO();
        rateDTO.setRateName(NAME);
        rateDTO.setPrice(PRICE);
        rateDTO.setProductId(ID);
        RateAddRequestDTO expectedRateDTO = new RateAddRequestDTO(ID, NAME, PRICE, ID);

        when(connection.prepareStatement(INSERT_RATE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(ID);

        RateAddRequestDTO resultRateDTO = repository.addRateByProductId(rateDTO);
        assertEquals(expectedRateDTO, resultRateDTO);

        verify(preparedStatement).setString(1, NAME);
        verify(preparedStatement).setDouble(2, PRICE);
        verify(preparedStatement).setLong(3, ID);
        verify(preparedStatement).execute();
        verify(resultSet).next();
    }

    @Test(expected = RateException.class)
    public void addRateByProductIdWhenThrowRateException() throws SQLException {
        RateAddRequestDTO rateDTO = new RateAddRequestDTO();
        rateDTO.setRateName(NAME);
        rateDTO.setPrice(PRICE);
        rateDTO.setProductId(ID);

        when(connection.prepareStatement(INSERT_RATE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(SQLException.class);

        repository.addRateByProductId(rateDTO);

        verify(preparedStatement).setString(1, NAME);
        verify(preparedStatement).setDouble(2, PRICE);
        verify(preparedStatement).setLong(3, ID);
    }

    @Test
    public void deleteRateById() throws SQLException {
        Long result = repository.deleteRateById(ID);
        assertEquals(ID, result);

        verify(statement).execute(DELETE_RATE);
    }

    @Test
    public void checkUsingRateBySubscribersWhenFound() throws SQLException {
        String login1 = "login1";
        String login2 = "login2";
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(ID);
        subscriber1.setLogin(login1);
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        subscriber2.setLogin(login2);
        List<Subscriber> expected = Arrays.asList(subscriber1, subscriber2);

        when(statement.executeQuery(GET_SUBSCRIBER)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(ID).thenReturn(2L);
        when(resultSet.getString("login")).thenReturn(login1).thenReturn(login2);

        List<Subscriber> resultList = repository.checkUsingRateBySubscribers(ID);
        assertEquals(expected, resultList);
    }

    @Test
    public void checkUsingRateBySubscribersWhenNotFound() throws SQLException {
        List<Subscriber> expected = Collections.emptyList();

        when(statement.executeQuery(GET_SUBSCRIBER)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Subscriber> resultList = repository.checkUsingRateBySubscribers(ID);
        assertEquals(expected, resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void doUnusableRateByRateId() throws SQLException {
        Rate rate = new Rate(ID, NAME, PRICE, ID, false);

        Rate resultRate = repository.doUnusableRateByRateId(rate);
        assertEquals(rate, resultRate);

        verify(statement).execute(DO_UNUSABLE_RATE);
    }
}