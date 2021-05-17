package org.test.project.infra.web;

import org.test.project.User.UserLoginException;
import org.test.project.operator.FiledChangeRate;
import org.test.project.subscriber.FiledTransactionException;
import org.test.project.subscriber.SubscriberException;

public class ExceptionHandlerImplMy implements ExceptionHandler {
    @Override
    public ModelAndView handle(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/error/ex.jsp");
        if (exception instanceof UserLoginException || exception instanceof FiledChangeRate
        || exception instanceof FiledTransactionException || exception instanceof SubscriberException) {
            modelAndView.addAttribute("message", exception.getMessage());
            return modelAndView;
        }

        return ModelAndView.withView("/error/internalerror.jsp");
    }
}
