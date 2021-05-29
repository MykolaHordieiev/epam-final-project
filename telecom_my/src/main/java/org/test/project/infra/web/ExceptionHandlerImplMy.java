package org.test.project.infra.web;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ExceptionHandlerImplMy implements ExceptionHandler {

    private final Map<Predicate<Exception>, Function<Exception, ModelAndView>> exceptionHandler;

    @Override
    public ModelAndView handle(Exception exception) {
        return exceptionHandler.entrySet()
                .stream()
                .filter(entry -> entry.getKey().test(exception))
                .findFirst()
                .map(entry -> entry.getValue())
                .map(func -> func.apply(exception))
                .orElse(ModelAndView.withView("/error/internalerror.jsp"));
    }
}
