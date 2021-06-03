package org.test.project.infra.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationFilterTest {


    private ServletRequest servletRequest;

    private ServletResponse servletResponse;
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

    @InjectMocks
    private AuthorizationFilter authFilter;

    @Before
    public void init() throws IOException, ServletException {
        authFilter.init(filterConfig);

     //  when(servletRequest).thenReturn(request);
       // when(((HttpServletResponse) servletRequest)).thenReturn(response);
        when(request.getContextPath()).thenReturn("/service/subscriber/all");
        when(request.getRequestURI()).thenReturn("/service/subscriber/all");
        when(request.getSession(false)).thenReturn(session);

        //  pathMatchers.add(new AuthorizationMatcher("/service/subscriber/all", UserRole.OPERATOR));

    }

    @Test
    public void doFilterWhen() throws IOException, ServletException {
        User subscriber = new Subscriber();
        when(session.getAttribute("user")).thenReturn(subscriber);

        authFilter.doFilter(servletRequest, servletResponse, filterChain);

    }
}