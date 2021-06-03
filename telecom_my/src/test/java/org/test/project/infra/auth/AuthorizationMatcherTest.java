package org.test.project.infra.auth;

import org.junit.Before;
import org.junit.Test;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;
import org.test.project.user.User;
import org.test.project.user.UserRole;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AuthorizationMatcherTest {

    private AuthorizationMatcher au;

    @Before
    public void init() {
        String pathRegex = "/operator/home.jsp";
        au = new AuthorizationMatcher(pathRegex, UserRole.OPERATOR);
    }

    @Test
    public void pathMatch() {
        String expectedTrue = "/operator/home.jsp";
        String expectedFalse = "/subscriber/home.jsp";

        boolean resultWithExpectedTrue = au.pathMatch(expectedTrue);
        boolean resultWithExpectedFalse = au.pathMatch(expectedFalse);

        assertTrue(resultWithExpectedTrue);
        assertFalse(resultWithExpectedFalse);
    }

    @Test
    public void hasRole() {
        User subscriber = new Subscriber();
        User operator = new Operator();
        operator.setUserRole(UserRole.OPERATOR);

        boolean resultWithSubscriber = au.hasRole(subscriber);
        boolean resultWithOperator = au.hasRole(operator);

        assertFalse(resultWithSubscriber);
        assertTrue(resultWithOperator);
    }
}