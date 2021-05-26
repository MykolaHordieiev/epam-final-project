package org.test.project.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Test
    public void loginUserReturnSubscriber() {
        User subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);
        subscriber.setPassword(PASSWORD);

        when(userRepository.getUserByLogin(LOGIN)).thenReturn(
                Optional.of(subscriber));

        User resultUser = userService.loginUser(LOGIN, PASSWORD);
        Assert.assertEquals(subscriber, resultUser);

        verify(userRepository).getUserByLogin(LOGIN);
    }

    @Test
    public void loginUserReturnOperator() {
        User operator = new Operator();
        operator.setPassword(PASSWORD);
        operator.setLogin(LOGIN);

        when(userRepository.getUserByLogin(LOGIN)).thenReturn(Optional.of(operator));

        User resultUser = userService.loginUser(LOGIN, PASSWORD);
        Assert.assertEquals(operator, resultUser);

        verify(userRepository).getUserByLogin(LOGIN);
    }

    @Test(expected = UserLoginException.class)
    public void loginUserThrowExceptionWhenNotFindUser() {
        when(userRepository.getUserByLogin(anyString())).thenReturn(Optional.empty());
        userService.loginUser(LOGIN, PASSWORD);
        verify(userRepository).getUserByLogin(LOGIN);
    }

    @Test(expected = UserLoginException.class)
    public void loginUserThrowExceptionWhenPasswordNotEquals() {
        User user = new Subscriber();
        user.setLogin(LOGIN);
        user.setPassword("aaa");
        when(userRepository.getUserByLogin(anyString())).thenReturn(Optional.of(user));
        userService.loginUser(LOGIN, PASSWORD);
        verify(userRepository).getUserByLogin(LOGIN);
    }
}
