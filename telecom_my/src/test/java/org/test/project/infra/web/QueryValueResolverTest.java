package org.test.project.infra.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.dto.SubscriberCreateDTO;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueryValueResolverTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private Enumeration<String> parameterNames;

    @InjectMocks
    private QueryValueResolver queryValueResolver;

    private SubscriberCreateDTO dto;
    private final Map<String, String> valuesMap = new HashMap<>();

    private static final String LOGIN = "petrov@As";
    private static final String PASSWORD = "0000like";

    @Before
    public void init() {
        dto = new SubscriberCreateDTO();
        dto.setLogin(LOGIN);
        dto.setPassword(PASSWORD);

        valuesMap.put("login", LOGIN);
        valuesMap.put("password", PASSWORD);

        when(request.getParameterNames()).thenReturn(parameterNames);
        when(parameterNames.hasMoreElements()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(parameterNames.nextElement()).thenReturn("login").thenReturn("password");
        when(request.getParameter("login")).thenReturn(LOGIN);
        when(request.getParameter("password")).thenReturn(PASSWORD);
        when(objectMapper.convertValue(valuesMap, SubscriberCreateDTO.class)).thenReturn(dto);
    }

    @Test
    public void getObject() {
        SubscriberCreateDTO result = queryValueResolver.getObject(request, SubscriberCreateDTO.class);
        assertEquals(dto, result);
    }
}