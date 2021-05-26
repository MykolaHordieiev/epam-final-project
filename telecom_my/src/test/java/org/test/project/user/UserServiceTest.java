package org.test.project.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Test
    public void loginUserReturnSubscriber() {
        String login = "a";
        String password = "a";
        User subscriber = new Subscriber();
        subscriber.setLogin(login);
        subscriber.setPassword(password);

        when(userRepository.getUserByLogin(login)).thenReturn(
                Optional.of(subscriber));

        User resultUser = userService.loginUser(login, password);
        Assert.assertEquals(subscriber, resultUser);

        verify(userRepository).getUserByLogin(login);
    }

    @Test
    public void loginUserReturnOperator() {
        String login = "admin";
        String password = "admin";
        User operator = new Operator();
        operator.setPassword(password);
        operator.setLogin(login);

        when(userRepository.getUserByLogin(login)).thenReturn(Optional.of(operator));

        User resultUser = userService.loginUser(login, password);
        Assert.assertEquals(operator, resultUser);

        verify(userRepository).getUserByLogin(login);
    }

    @Test(expected = UserLoginException.class)
    public void loginUserThrowExceptionWhenNotFindUser() {
        String login = "a";
        when(userRepository.getUserByLogin(anyString())).thenReturn(Optional.empty());
        userService.loginUser(login, "a");
        verify(userRepository).getUserByLogin(login);
    }

    @Test(expected = UserLoginException.class)
    public void loginUserThrowExceptionWhenPasswordNotEquals() {
        String login = "a";
        String password = "aa";
        User user = new Subscriber();
        user.setLogin(login);
        user.setPassword("aaa");
        when(userRepository.getUserByLogin(anyString())).thenReturn(Optional.of(user));
        userService.loginUser(login, password);
        verify(userRepository).getUserByLogin(login);
    }
}
