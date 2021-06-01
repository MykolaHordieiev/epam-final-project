package org.test.project.infra.config;

import org.test.project.infra.exception.TelecomException;
import org.test.project.infra.web.ExceptionHandlerImplMy;
import org.test.project.infra.web.ModelAndView;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExceptionHandlerConfig {

    public ExceptionHandlerImplMy configureExceptionHandler() {
        Map<Predicate<Exception>, Function<Exception, ModelAndView>> exceptionHandler = new HashMap<>();
        exceptionHandler.put(exception -> exception instanceof TelecomException, this::getViewForHandledException);
        return new ExceptionHandlerImplMy(exceptionHandler);
    }

    private ModelAndView getViewForHandledException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/error/ex.jsp");
        modelAndView.addAttribute("message", ex.getMessage());
        return modelAndView;
    }
}
