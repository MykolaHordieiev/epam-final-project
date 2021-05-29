package org.test.project.subscriber;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private static final String GET_BY_LOGIN = "SELECT id FROM user WHERE user.login='login'";
    private static final String INSERT_INTO_USER = "INSERT INTO user (login,password,role) VALUES (?,?,?);";
    private static final String INSERT_INTO_SUBSCRIBER = "INSERT INTO subscriber (id) VALUES (?)";
    private static final String GET_ALL = "SELECT * FROM user JOIN subscriber ON user.id=subscriber.id";
    private static final String LOCK_SUBSCRIBER = "UPDATE subscriber SET locked=true WHERE id=1";
    private static final String UNLOCK_SUBSCRIBER = "UPDATE subscriber SET locked=false WHERE id=1";
    private static final String REPLENISH_BALANCE = "UPDATE subscriber SET balance=? WHERE id=1";
    private final Long ID = 1L;
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final Double BALANCE = 20d;

    @SneakyThrows
    @Before
    public void setUp() {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @SneakyThrows
    @Test
    public void getByIdWhenFoundSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);

        when(statement.executeQuery(GET_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("login")).thenReturn(LOGIN);
        when(resultSet.getDouble("balance")).thenReturn(BALANCE);
        when(resultSet.getBoolean("locked")).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getById(subscriber);
        assertNotNull(resultSubscriber);
        assertTrue(resultSubscriber.isPresent());
        assertEquals(subscriber, resultSubscriber.get());

        verify(statement).executeQuery(GET_BY_ID);
        verify(resultSet).next();
        verify(resultSet).getString("login");
        verify(resultSet).getDouble("balance");
        verify(resultSet).getBoolean("locked");
    }

    @SneakyThrows
    @Test
    public void getByIdWhenNotFoundSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);

        when(statement.executeQuery(GET_BY_ID)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getById(subscriber);
        assertFalse(resultSubscriber.isPresent());

        verify(statement).executeQuery(GET_BY_ID);
        verify(resultSet).next();
    }

    @SneakyThrows
    @Test
    public void getByLoginWhenFoundSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);

        when(statement.executeQuery(GET_BY_LOGIN)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ID);

        Optional<Subscriber> resultSubscriber = repository.getByLogin(subscriber);
        assertNotNull(resultSubscriber);
        assertTrue(resultSubscriber.isPresent());
        assertEquals(subscriber, resultSubscriber.get());

        verify(statement).executeQuery(GET_BY_LOGIN);
        verify(resultSet).next();
        verify(resultSet).getLong("id");
    }

    @SneakyThrows
    @Test
    public void getByLoginWhenNotFoundSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);

        when(statement.executeQuery(GET_BY_LOGIN)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Subscriber> resultSubscriber = repository.getByLogin(subscriber);
        assertNotNull(resultSubscriber);
        assertFalse(resultSubscriber.isPresent());

        verify(statement).executeQuery(GET_BY_LOGIN);
        verify(resultSet).next();
    }

    @SneakyThrows
    @Test
    public void insertSubscriberIsCommit() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);
        subscriber.setPassword(PASSWORD);

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(ID);
        when(connection.prepareStatement(INSERT_INTO_SUBSCRIBER)).thenReturn(preparedStatement1);

        Subscriber resultSubscriber = repository.insertSubscriber(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(subscriber, resultSubscriber);

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

    @SneakyThrows
    @Test(expected = SubscriberException.class)
    public void insertSubscriberIsRollback() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);
        subscriber.setPassword(PASSWORD);

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(SQLException.class);

        repository.insertSubscriber(subscriber);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setString(1, LOGIN);
        verify(preparedStatement).setString(2, PASSWORD);
        verify(preparedStatement).setString(3, "SUBSCRIBER");
        verify(preparedStatement).execute();
        verify(connection).rollback();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @SneakyThrows
    @Test
    public void getAllWhenFoundSubscribers() {
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

    @SneakyThrows
    @Test
    public void getAllWhenNotFoundSubscribers() {
        List<Subscriber> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_ALL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Subscriber> resultList = repository.getAll();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
        assertTrue(resultList.isEmpty());
    }

    @SneakyThrows
    @Test
    public void lockSubById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        when(connection.createStatement()).thenReturn(statement);

        Subscriber resultSubscriber = repository.lockSubById(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(subscriber, resultSubscriber);
        assertTrue(resultSubscriber.isLock());

        verify(statement).execute(LOCK_SUBSCRIBER);
    }

    @SneakyThrows
    @Test
    public void unlockSubById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        when(connection.createStatement()).thenReturn(statement);

        Subscriber resultSubscriber = repository.unlockSubById(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(subscriber, resultSubscriber);
        assertFalse(resultSubscriber.isLock());

        verify(statement).execute(UNLOCK_SUBSCRIBER);
    }

    @SneakyThrows
    @Test
    public void topUpBalanceById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);

        when(connection.prepareStatement(REPLENISH_BALANCE)).thenReturn(preparedStatement);

        Subscriber resultSubscriber = repository.topUpBalanceById(subscriber, BALANCE);
        assertNotNull(resultSubscriber);
        assertEquals(subscriber, resultSubscriber);

        verify(preparedStatement).setDouble(1, BALANCE);
        verify(preparedStatement).execute();
    }
}