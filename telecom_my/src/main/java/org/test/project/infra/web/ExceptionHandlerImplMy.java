package org.test.project.infra.web;

import org.test.project.User.UserLoginException;
import org.test.project.product.ProductException;
import org.test.project.rate.RateException;
import org.test.project.subscriber.FiledTransactionException;
import org.test.project.subscriber.SubscriberException;
import org.test.project.subscribing.SubscribingException;

public class ExceptionHandlerImplMy implements ExceptionHandler {
    @Override
    public ModelAndView handle(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/error/ex.jsp");
        if (exception instanceof UserLoginException || exception instanceof FiledTransactionException
                || exception instanceof SubscriberException || exception instanceof RateException
                || exception instanceof SubscribingException || exception instanceof ProductException) {
            modelAndView.addAttribute("message", exception.getMessage());
            return modelAndView;
        }

        return ModelAndView.withView("/error/internalerror.jsp");
    }
}
