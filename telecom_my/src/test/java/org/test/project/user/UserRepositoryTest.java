package org.test.project.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;
import org.test.project.user.dto.UserLoginDTO;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static UserLoginDTO userLoginDTO = new UserLoginDTO(LOGIN, PASSWORD);

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getString("password")).thenReturn(PASSWORD);
    }

    @Test
    public void getUserByLoginWhenReturnedSubscriber() throws SQLException {
        User expectedSubscriber = new Subscriber();
        expectedSubscriber.setId(ID);
        expectedSubscriber.setLogin(LOGIN);
        expectedSubscriber.setPassword(PASSWORD);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("SUBSCRIBER");

        Optional<User> resultSubscriber = userRepository.getUserByLogin(userLoginDTO);
        assertNotNull(resultSubscriber);
        assertTrue(resultSubscriber.isPresent());
        assertEquals(expectedSubscriber, resultSubscriber.get());

        verify(preparedStatement).setString(1, LOGIN);
        verify(connection).prepareStatement(QUERY);
    }

    @Test
    public void getUserByLoginWhenReturnedOperator() throws SQLException {
        User expectedOperator = new Operator(ID, LOGIN, PASSWORD, UserRole.OPERATOR);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("OPERATOR");

        Optional<User> resultOperator = userRepository.getUserByLogin(userLoginDTO);
        assertNotNull(resultOperator);
        assertTrue(resultOperator.isPresent());
        assertEquals(expectedOperator, resultOperator.get());

        verify(preparedStatement).setString(1, LOGIN);
    }

    @Test
    public void getUserByLoginWhenNotFoundUser() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        Optional<User> resultUser = userRepository.getUserByLogin(userLoginDTO);
        assertNotNull(resultUser);
        assertFalse(resultUser.isPresent());

        verify(preparedStatement).setString(1, LOGIN);
    }
}