package org.test.project.infra.config;

import org.test.project.infra.web.ExceptionHandlerImplMy;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.WebException;
import org.test.project.product.ProductException;
import org.test.project.rate.RateException;
import org.test.project.subscriber.SubscriberException;
import org.test.project.subscribing.SubscribingException;
import org.test.project.user.UserLoginException;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExceptionHandlerConfig {

    public ExceptionHandlerImplMy configureExceptionHandler() {
        Map<Predicate<Exception>, Function<Exception, ModelAndView>> exceptionHandler = new HashMap<>();
        exceptionHandler.put(this::getExceptions, this::getViewForHandledException);
        return new ExceptionHandlerImplMy(exceptionHandler);
    }

    private boolean getExceptions(Exception exception) {
        List<Boolean> instanceList = new ArrayList<>();
        instanceList.add(exception instanceof UserLoginException);
        instanceList.add(exception instanceof SubscriberException);
        instanceList.add(exception instanceof SubscribingException);
        instanceList.add(exception instanceof ProductException);
        instanceList.add(exception instanceof RateException);
        instanceList.add(exception instanceof WebException);
        return instanceList.stream().filter(instance -> instance.equals(true))
                .findFirst().orElse(false);
    }

    private ModelAndView getViewForHandledException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/error/ex.jsp");
        modelAndView.addAttribute("message", ex.getMessage());
        return modelAndView;
    }
}
