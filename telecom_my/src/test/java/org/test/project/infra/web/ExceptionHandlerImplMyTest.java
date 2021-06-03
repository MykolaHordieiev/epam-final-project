package org.test.project.infra.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.exception.TelecomException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionHandlerImplMyTest {

    @Mock
    private Map<Predicate<Exception>, Function<Exception, ModelAndView>> exceptionHandler;

    private Map<Predicate<Exception>, Function<Exception, ModelAndView>> exceptionHandler1 = new HashMap<>();

    @InjectMocks
    ExceptionHandlerImplMy handlerImpl;

    @Before
    public void init() {
        exceptionHandler1.put(exception -> exception instanceof TelecomException, view -> ModelAndView.withView("/error/ex.jsp"));
        when(exceptionHandler.entrySet()).thenReturn(exceptionHandler1.entrySet());
    }

    @Test
    public void handle() {
        ModelAndView modelAndView = handlerImpl.handle(new RuntimeException());
        assertEquals("/error/internalerror.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        ModelAndView modelAndView1 = handlerImpl.handle(new TelecomException("message"));
        assertEquals("/error/ex.jsp", modelAndView1.getView());
        assertFalse(modelAndView.isRedirect());
    }
}