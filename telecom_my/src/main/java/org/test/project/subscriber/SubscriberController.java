package org.test.project.subscriber;


import lombok.AllArgsConstructor;
import org.test.project.User.User;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@AllArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;

    public ModelAndView getSubscriberById(HttpServletRequest request, HttpServletResponse response) {
        String idFromRequest = request.getParameter("id");
        validEntryParameter(idFromRequest, "id");
        Long id = Long.parseLong(idFromRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/infobyid.jsp");
        modelAndView.addAttribute("subscriber", subscriberService.getSubscriberById(id));
        modelAndView.addAttribute("subscriptions", subscriberService.getSubscribing(id));
        return modelAndView;
    }

    public ModelAndView createSubscriber(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        validEntryParameter(login,"login");
        validEntryParameter(password,"password");
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(login);
        subscriber.setPassword(password);
        Subscriber createdSubscriber = subscriberService.create(subscriber);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + createdSubscriber.getId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView getAll(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/all.jsp");
        modelAndView.addAttribute("subscribers", subscriberService.getAll());
        return modelAndView;
    }

    public ModelAndView lockUser(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        subscriberService.lockSubscriberById(Long.parseLong(id));
        return getSubscriberById(request, response);
    }

    public ModelAndView unLockUser(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        subscriberService.unLockSubscriberById(Long.parseLong(id));
        return getSubscriberById(request, response);
    }

    public ModelAndView topUpTheBalance(HttpServletRequest request, HttpServletResponse response) {
        String amount = validEntryParameter(request.getParameter("amount"),"amount");
        User subscriber = getUserOfSession(request);
        Subscriber returnedSubscriber = subscriberService.topUpBalance(subscriber.getId(), Double.parseDouble(amount));
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + returnedSubscriber.getId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private String validEntryParameter(String entryParameter,String parameter) {
        if (entryParameter.equals("")) {
            throw new SubscriberException("entry parameter cannot be empty: "+ parameter);
        }
        return entryParameter;
    }

    private Subscriber getUserOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Subscriber) session.getAttribute("user");
    }
}