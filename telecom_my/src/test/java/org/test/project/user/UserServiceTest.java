package org.test.project.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;
import org.test.project.user.dto.UserLoginDTO;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static UserLoginDTO userLoginDTO = new UserLoginDTO(LOGIN, PASSWORD);

    @Test
    public void loginUserReturnSubscriber() {
        User subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);
        subscriber.setPassword(PASSWORD);

        when(userRepository.getUserByLogin(userLoginDTO)).thenReturn(Optional.of(subscriber));

        User resultUser = userService.loginUser(userLoginDTO);
        Assert.assertEquals(subscriber, resultUser);
    }

    @Test
    public void loginUserReturnOperator() {
        User operator = new Operator();
        operator.setPassword(PASSWORD);
        operator.setLogin(LOGIN);

        when(userRepository.getUserByLogin(userLoginDTO)).thenReturn(Optional.of(operator));

        User resultUser = userService.loginUser(userLoginDTO);
        Assert.assertEquals(operator, resultUser);
    }

    @Test(expected = UserLoginException.class)
    public void loginUserThrowExceptionWhenNotFindUser() {
        when(userRepository.getUserByLogin(userLoginDTO)).thenReturn(Optional.empty());
        userService.loginUser(userLoginDTO);
    }

    @Test(expected = UserLoginException.class)
    public void loginUserThrowExceptionWhenPasswordNotEquals() {
        User foundUser = new Subscriber();
        foundUser.setLogin(LOGIN);
        foundUser.setPassword("aaa");
        when(userRepository.getUserByLogin(userLoginDTO)).thenReturn(Optional.of(foundUser));
        userService.loginUser(userLoginDTO);
    }
}
