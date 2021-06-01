package org.test.project.subscriber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;

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
public class SubscriberRepositoryTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private PreparedStatement preparedStatement1;
    @Mock
    private Statement statement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private SubscriberRepository repository;

    private static final String GET_BY_ID = "SELECT * FROM user JOIN subscriber ON user.id=subscriber.id WHERE user.id=1";
    private static final String GET_BY_LOGIN = "SELECT user.id, balance, locked FROM user JOIN subscriber ON user.id=subscriber.id " +
            "WHERE user.login='login'";
    private static final String INSERT_INTO_USER = "INSERT INTO user (login,password,role) VALUES (?,?,?);";
    private static final String INSERT_INTO_SUBSCRIBER = "INSERT INTO subscriber (id) VALUES (?)";
    private static final String GET_ALL = "SELECT * FROM user JOIN subscriber ON user.id=subscriber.id";
    private static final String REPLENISH_BALANCE = "UPDATE subscriber SET balance=? WHERE id=1";
    private static final Long ID = 1L;
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final Double BALANCE = 20d;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    public void getByIdWhenFoundSubscriber() throws SQLException {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        subscriber.setLogin(LOGIN);
        subscriber.setLock(false);
        subscriber.setBalance(BALANCE);

        when(statement.executeQuery(GET_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("login")).thenReturn(LOGIN);
        when(resultSet.getDouble("balance")).thenReturn(BALANCE);
        when(resultSet.getBoolean("locked")).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getById(ID);
        assertNotNull(resultSubscriber);
        assertTrue(resultSubscriber.isPresent());
        assertEquals(subscriber, resultSubscriber.get());
    }

    @Test
    public void getByIdWhenNotFoundSubscriber() throws SQLException {
        when(statement.executeQuery(GET_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getById(ID);
        assertFalse(resultSubscriber.isPresent());

        verify(statement).executeQuery(GET_BY_ID);
        verify(resultSet).next();
    }

    @Test
    public void getByLoginWhenFoundSubscriber() throws SQLException {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        subscriber.setLogin(LOGIN);
        subscriber.setLock(false);
        subscriber.setBalance(BALANCE);

        when(statement.executeQuery(GET_BY_LOGIN)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getDouble("balance")).thenReturn(BALANCE);
        when(resultSet.getBoolean("locked")).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getByLogin(LOGIN);
        assertNotNull(resultSubscriber);
        assertTrue(resultSubscriber.isPresent());
        assertEquals(subscriber, resultSubscriber.get());
    }

    @Test
    public void getByLoginWhenNotFoundSubscriber() throws SQLException {
        when(statement.executeQuery(GET_BY_LOGIN)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getByLogin(LOGIN);
        assertNotNull(resultSubscriber);
        assertFalse(resultSubscriber.isPresent());

        verify(statement).executeQuery(GET_BY_LOGIN);
        verify(resultSet).next();
    }

    @Test
    public void insertSubscriberIsCommit() throws SQLException {
        SubscriberCreateDTO expectedDTO = new SubscriberCreateDTO(ID, LOGIN, PASSWORD);

        SubscriberCreateDTO subscriberDTO = new SubscriberCreateDTO();
        subscriberDTO.setLogin(LOGIN);
        subscriberDTO.setPassword(PASSWORD);

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(ID);
        when(connection.prepareStatement(INSERT_INTO_SUBSCRIBER)).thenReturn(preparedStatement1);

        SubscriberCreateDTO resultSubscriberDTO = repository.insertSubscriber(subscriberDTO);
        assertEquals(expectedDTO, resultSubscriberDTO);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setString(1, LOGIN);
        verify(preparedStatement).setString(2, PASSWORD);
        verify(preparedStatement).setString(3, "SUBSCRIBER");
        verify(preparedStatement).execute();
        verify(resultSet).next();
        verify(preparedStatement1).setLong(1, ID);
        verify(preparedStatement1).execute();
        verify(connection).commit();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = SubscriberException.class)
    public void insertSubscriberIsRollback() throws SQLException {
        SubscriberCreateDTO subscriberDTO = new SubscriberCreateDTO();
        subscriberDTO.setLogin(LOGIN);
        subscriberDTO.setPassword(PASSWORD);

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(SQLException.class);

        repository.insertSubscriber(subscriberDTO);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setString(1, LOGIN);
        verify(preparedStatement).setString(2, PASSWORD);
        verify(preparedStatement).setString(3, "SUBSCRIBER");
        verify(preparedStatement).execute();
        verify(connection).rollback();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void getAllWhenFoundSubscribers() throws SQLException {
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(ID);
        subscriber1.setLogin(LOGIN);
        subscriber1.setBalance(BALANCE);
        subscriber1.setLock(false);
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        subscriber2.setLogin("me");
        subscriber2.setBalance(10d);
        subscriber2.setLock(false);
        List<Subscriber> expectedList = Arrays.asList(subscriber1, subscriber2);

        when(connection.prepareStatement(GET_ALL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(ID).thenReturn(2L);
        when(resultSet.getString("login")).thenReturn(LOGIN).thenReturn("me");
        when(resultSet.getDouble("balance")).thenReturn(BALANCE).thenReturn(10d);
        when(resultSet.getBoolean("locked")).thenReturn(false);

        List<Subscriber> resultList = repository.getAll();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
        assertEquals(2, resultList.size());
    }

    @Test
    public void getAllWhenNotFoundSubscribers() throws SQLException {
        List<Subscriber> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_ALL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Subscriber> resultList = repository.getAll();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void lockSubById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        subscriber.setLock(true);

        Subscriber resultSubscriber = repository.lockSubById(ID);
        assertEquals(subscriber, resultSubscriber);
        assertTrue(resultSubscriber.isLock());
    }

    @Test
    public void unlockSubById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        subscriber.setLock(false);

        Subscriber resultSubscriber = repository.unlockSubById(ID);
        assertEquals(subscriber, resultSubscriber);
        assertFalse(resultSubscriber.isLock());
    }

    @Test
    public void replenishBalanceById() throws SQLException {
        SubscriberReplenishDTO replenishDTO = new SubscriberReplenishDTO(ID, BALANCE);

        when(connection.prepareStatement(REPLENISH_BALANCE)).thenReturn(preparedStatement);

        SubscriberReplenishDTO resultSubscriberDTO = repository.replenishBalanceById(replenishDTO);
        assertEquals(replenishDTO, resultSubscriberDTO);

        verify(preparedStatement).setDouble(1, BALANCE);
        verify(preparedStatement).execute();
    }
}