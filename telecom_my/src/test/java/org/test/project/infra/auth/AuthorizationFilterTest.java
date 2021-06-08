package org.test.project.infra.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;
import org.test.project.user.User;
import org.test.project.user.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationFilterTest {

    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private AuthorizationFilter authFilter;

    @Before
    public void init() {
        authFilter.init(filterConfig);

        when(request.getContextPath()).thenReturn("/telecom");
        when(request.getRequestURI()).thenReturn("/telecom/service/subscriber/all");
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    public void doFilterWhenHasNoAccess() throws IOException, ServletException {
        User subscriber = new Subscriber();
        when(session.getAttribute("user")).thenReturn(subscriber);
        when(request.getRequestDispatcher("/error/forbiden.jsp")).thenReturn(requestDispatcher);

        authFilter.doFilter(request, response, filterChain);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doFilterWhenHasAccess() throws IOException, ServletException {
        User operator = new Operator();
        operator.setUserRole(UserRole.OPERATOR);

        when(session.getAttribute("user")).thenReturn(operator);

        authFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterWhenUserIsNull() throws IOException, ServletException {
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getRequestDispatcher("/error/forbiden.jsp")).thenReturn(requestDispatcher);

        authFilter.doFilter(request, response, filterChain);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doFilterWhenPathNoMatch() throws IOException, ServletException {
        User subscriber = new Subscriber();

        when(request.getRequestURI()).thenReturn("/telecom/service/subscriber/all.jsp");
        when(session.getAttribute("user")).thenReturn(subscriber);

        authFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

    }

}