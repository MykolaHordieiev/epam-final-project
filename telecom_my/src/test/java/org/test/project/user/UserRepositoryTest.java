package org.test.project.user;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {
    @Mock
    private DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;

    @InjectMocks
    UserRepository userRepository;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String QUERY = "SELECT * FROM user WHERE login=?";
    private static final Long ID = 1L;

    @SneakyThrows
    @Before
    public void setUp() {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getString("password")).thenReturn(PASSWORD);
    }

    @SneakyThrows
    @Test
    public void getUserByLoginWhenReturnedSubscriber() {
        User expectedSubscriber = new Subscriber();
        expectedSubscriber.setId(ID);
        expectedSubscriber.setLogin(LOGIN);
        expectedSubscriber.setPassword(PASSWORD);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("SUBSCRIBER");

        Optional<User> resultSubscriber = userRepository.getUserByLogin(LOGIN);
        assertNotNull(resultSubscriber);
        assertTrue(resultSubscriber.isPresent());
        assertEquals(expectedSubscriber, resultSubscriber.get());

        verify(preparedStatement).setString(1, LOGIN);
        verify(connection).prepareStatement(QUERY);
    }

    @SneakyThrows
    @Test
    public void getUserByLoginWhenReturnedOperator() {
        User expectedOperator = new Operator(ID, LOGIN, PASSWORD, UserRole.OPERATOR);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("OPERATOR");

        Optional<User> resultOperator = userRepository.getUserByLogin(LOGIN);
        assertNotNull(resultOperator);
        assertTrue(resultOperator.isPresent());
        assertEquals(expectedOperator, resultOperator.get());

        verify(preparedStatement).setString(1, LOGIN);
    }

    @SneakyThrows
    @Test
    public void getUserByLoginWhenNotFoundUser() {
        when(resultSet.next()).thenReturn(false);

        Optional<User> resultUser = userRepository.getUserByLogin(LOGIN);
        assertNotNull(resultUser);
        assertFalse(resultUser.isPresent());

        verify(preparedStatement).setString(1, LOGIN);
    }
}