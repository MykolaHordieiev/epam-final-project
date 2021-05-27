package org.test.project.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;

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

    private HttpServletResponse response;

    @InjectMocks
    private UserController userController;

    private static final String LOGIN = "test";
    private static final String PASSWORD = "tests";
    private static final String SELECTED_LOCALE = "selectedLocale";
    private static final String VIEW = "/index.jsp";
    private static final Long ID = 1L;

    @Before
    public void initRequest() {
        when(request.getParameter("selectedLocale")).thenReturn(SELECTED_LOCALE);
        when(request.getParameter("view")).thenReturn(VIEW);
        when(request.getParameter("login")).thenReturn(LOGIN);
        when(request.getParameter("password")).thenReturn(PASSWORD);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
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
        ModelAndView modelAndView = userController.changeLocale(request, response);
        assertNotNull(modelAndView);
        assertEquals(VIEW, modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request, times(2)).getParameter(anyString());
        verify(session).setAttribute("selectedLocale", new Locale(SELECTED_LOCALE));
        verify(request).getSession(false);
    }

    @Test
    public void loginWhenServiceReturnSubscriber() {
        User user = new Subscriber();
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        user.setUserRole(UserRole.SUBSCRIBER);
        user.setId(ID);

        when(validator.checkEmptyLogin(LOGIN)).thenReturn(LOGIN);
        when(validator.checkEmptyEntryPassword(PASSWORD)).thenReturn(PASSWORD);
        when(userService.loginUser(LOGIN, PASSWORD)).thenReturn(user);
        when(viewMap.get(user.getUserRole())).thenReturn("/subscriber/home.jsp");

        ModelAndView modelAndView = userController.login(request, response);
        Assert.assertNotNull(modelAndView);
        Assert.assertEquals("/subscriber/home.jsp", modelAndView.getView());
        Assert.assertTrue(modelAndView.isRedirect());

        verify(request, times(2)).getParameter(anyString());
        verify(validator).checkEmptyLogin(LOGIN);
        verify(validator).checkEmptyEntryPassword(PASSWORD);
        verify(userService).loginUser(LOGIN, PASSWORD);
        verify(request).getSession();
        verify(session).setAttribute("user", user);
    }

    @Test
    public void loginWhenServiceReturnOperator() {
        User user = new Operator();
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        user.setUserRole(UserRole.OPERATOR);

        when(validator.checkEmptyLogin(LOGIN)).thenReturn(LOGIN);
        when(validator.checkEmptyEntryPassword(PASSWORD)).thenReturn(PASSWORD);
        when(userService.loginUser(LOGIN, PASSWORD)).thenReturn(user);
        when(viewMap.get(user.getUserRole())).thenReturn("/operator/home.jsp");

        ModelAndView modelAndView = userController.login(request, response);
        assertNotNull(modelAndView);
        assertEquals("/operator/home.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request, times(2)).getParameter(anyString());
        verify(validator).checkEmptyLogin(LOGIN);
        verify(validator).checkEmptyEntryPassword(PASSWORD);
        verify(userService).loginUser(LOGIN, PASSWORD);
        verify(request).getSession();
        verify(session).setAttribute("user", user);
    }

    @Test
    public void logout() {
        ModelAndView modelAndView = userController.logout(request, response);

        assertNotNull(modelAndView);
        assertEquals("/index.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request, atLeastOnce()).getSession(false);
    }
}