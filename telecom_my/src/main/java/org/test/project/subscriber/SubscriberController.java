package org.test.project.subscriber;


import lombok.AllArgsConstructor;
import org.test.project.User.User;
import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.subscribing.SubscribingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SubscriberController implements Controller {

    private final SubscriberService subscriberService;
    private final SubscribingService subscribingService;
    private List<RequestMatcher> requestMatchers;

    public SubscriberController(SubscriberService subscriberService, SubscribingService subscribingService) {
        this.subscriberService = subscriberService;
        this.subscribingService = subscribingService;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/subscriber", "GET", this::getSubscriberById));
        requestMatchers.add(new RequestMatcher("/subscriber", "POST", this::createSubscriber));
        requestMatchers.add(new RequestMatcher("/subscriber/all", "GET", this::getAll));
        requestMatchers.add(new RequestMatcher("/subscriber/lock", "POST", this::lockSubscriber));
        requestMatchers.add(new RequestMatcher("/subscriber/unlock", "POST", this::unLockSubscriber));
        requestMatchers.add(new RequestMatcher("/subscriber/balance", "POST", this::topUpTheBalance));
        return requestMatchers;
    }

    public ModelAndView getSubscriberById(HttpServletRequest request, HttpServletResponse response) {
        String subscriberId = validEntryParameter(request.getParameter("id"), "id");
        Long id = Long.parseLong(subscriberId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/infobyid.jsp");
        modelAndView.addAttribute("subscriber", subscriberService.getSubscriberById(id));
        modelAndView.addAttribute("subscriptions", subscribingService.getSubscribing(id));
        return modelAndView;
    }

    public ModelAndView createSubscriber(HttpServletRequest request, HttpServletResponse response) {
        String login = validEntryParameter(request.getParameter("login"), "login");
        String password = validEntryParameter(request.getParameter("password"), "password");
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

    public ModelAndView lockSubscriber(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        subscriberService.lockSubscriberById(Long.parseLong(id));
        return getSubscriberById(request, response);
    }

    public ModelAndView unLockSubscriber(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        subscriberService.unLockSubscriberById(Long.parseLong(id));
        return getSubscriberById(request, response);
    }

    public ModelAndView topUpTheBalance(HttpServletRequest request, HttpServletResponse response) {
        String amount = validEntryParameter(request.getParameter("amount"), "amount");
        User subscriber = getUserOfSession(request);
        Subscriber returnedSubscriber = subscriberService.topUpBalance(subscriber.getId(), Double.parseDouble(amount));
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + returnedSubscriber.getId());
        HttpSession session = request.getSession(true);
        session.setAttribute("user", returnedSubscriber);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private String validEntryParameter(String entryParameter, String parameter) {
        if (entryParameter.equals("")) {
            throw new SubscriberException("entry parameter cannot be empty: " + parameter);
        }
        return entryParameter;
    }

    private Subscriber getUserOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Subscriber) session.getAttribute("user");
    }
}