package org.test.project.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.QueryValueResolver;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;
import org.test.project.user.dto.UserLoginDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserValidator validator;
    @Mock
    private Map<UserRole, String> viewMap;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletResponse response;
    @Mock
    QueryValueResolver queryValueResolver;

    @InjectMocks
    private UserController userController;

    private static final String LOGIN = "test";
    private static final String PASSWORD = "tests";
    private static final String SELECTED_LOCALE = "selectedLocale";
    private static final String VIEW = "/index.jsp";
    private static final Long ID = 1L;
    private static UserLoginDTO userLoginDTO = new UserLoginDTO(LOGIN,PASSWORD);

    @Before
    public void initRequest() {
        when(queryValueResolver.getObject(request, UserLoginDTO.class)).thenReturn(userLoginDTO);
        when(validator.checkUser(userLoginDTO)).thenReturn(userLoginDTO);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    public void getRequestMatcher() {
        List<RequestMatcher> requestMatcherList = userController.getRequestMatcher();
        assertNotNull(requestMatcherList);
        assertFalse(requestMatcherList.isEmpty());
        assertEquals(3, requestMatcherList.size());
    }

    @Test
    public void changeLocale() {
        when(request.getParameter("selectedLocale")).thenReturn(SELECTED_LOCALE);
        when(request.getParameter("view")).thenReturn(VIEW);

        ModelAndView modelAndView = userController.changeLocale(request, response);
        assertNotNull(modelAndView);
        assertEquals(VIEW, modelAndView.getView());
        assertTrue(modelAndView.isRedirect());


        verify(session).setAttribute("selectedLocale", new Locale(SELECTED_LOCALE));
        verify(request).getSession(false);
    }

    @Test
    public void loginWhenServiceReturnSubscriber() {
        User user = new Subscriber();
        user.setId(ID);
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);

        when(userService.loginUser(userLoginDTO)).thenReturn(user);
        when(viewMap.get(user.getUserRole())).thenReturn("/subscriber/home.jsp");

        ModelAndView modelAndView = userController.login(request, response);
        Assert.assertNotNull(modelAndView);
        Assert.assertEquals("/subscriber/home.jsp", modelAndView.getView());
        Assert.assertTrue(modelAndView.isRedirect());

        verify(request).getSession();
        verify(session).setAttribute("user", user);
    }

    @Test
    public void loginWhenServiceReturnOperator() {
        User user = new Operator();
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        user.setUserRole(UserRole.OPERATOR);

        when(userService.loginUser(userLoginDTO)).thenReturn(user);
        when(viewMap.get(user.getUserRole())).thenReturn("/operator/home.jsp");

        ModelAndView modelAndView = userController.login(request, response);
        assertNotNull(modelAndView);
        assertEquals("/operator/home.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getSession();
        verify(session).setAttribute("user", user);
    }

    @Test
    public void logout() {
        ModelAndView modelAndView = userController.logout(request, response);
        assertNotNull(modelAndView);
        assertEquals("/index.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }
}