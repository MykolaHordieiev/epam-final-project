package org.test.project.subscriber;


import lombok.AllArgsConstructor;
import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.QueryValueResolver;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;
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
    private final SubscriberValidator validator;

    private final QueryValueResolver queryValueResolver;
    private List<RequestMatcher> requestMatchers;

    public SubscriberController(SubscriberService subscriberService, SubscribingService subscribingService,
                                SubscriberValidator validator, QueryValueResolver queryValueResolver) {
        this.subscriberService = subscriberService;
        this.subscribingService = subscribingService;
        this.validator = validator;
        this.queryValueResolver = queryValueResolver;
        requestMatchers = new ArrayList<>();
        requestMatchers.add(new RequestMatcher("/subscriber", "GET", this::getSubscriberById));
        requestMatchers.add(new RequestMatcher("/subscriber/bylogin", "GET", this::getSubscriberByLogin));
        requestMatchers.add(new RequestMatcher("/subscriber", "POST", this::createSubscriber));
        requestMatchers.add(new RequestMatcher("/subscriber/all", "GET", this::getAll));
        requestMatchers.add(new RequestMatcher("/subscriber/lock", "POST", this::lockSubscriber));
        requestMatchers.add(new RequestMatcher("/subscriber/unlock", "POST", this::unlockSubscriber));
        requestMatchers.add(new RequestMatcher("/subscriber/balance", "POST", this::replenishBalance));
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        return requestMatchers;
    }

    public ModelAndView getSubscriberById(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/infobyid.jsp");
        modelAndView.addAttribute("subscriber", subscriberService.getSubscriberById(id));
        modelAndView.addAttribute("subscriptions", subscribingService.getSubscribing(id));
        return modelAndView;
    }

    public ModelAndView getSubscriberByLogin(HttpServletRequest request, HttpServletResponse response) {
        String login = validator.checkEmptyLogin(request.getParameter("login"));
        Subscriber foundSubscriber = subscriberService.getSubscriberByLogin(login);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/infobyid.jsp");
        modelAndView.addAttribute("subscriber", foundSubscriber);
        modelAndView.addAttribute("subscriptions", subscribingService.getSubscribing(foundSubscriber.getId()));
        return modelAndView;
    }

    public ModelAndView createSubscriber(HttpServletRequest request, HttpServletResponse response) {
        SubscriberCreateDTO dto = queryValueResolver.getObject(request, SubscriberCreateDTO.class);
        validator.checkValidLoginPassword(dto);
        Subscriber createdSubscriber = subscriberService.create(dto);
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
        long id = Long.parseLong(request.getParameter("id"));
        subscriberService.lockSubscriberById(id);
        return getSubscriberById(request, response);
    }

    public ModelAndView unlockSubscriber(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        subscriberService.unlockSubscriberById(id);
        return getSubscriberById(request, response);
    }

    public ModelAndView replenishBalance(HttpServletRequest request, HttpServletResponse response) {
        String stringAmount = request.getParameter("amount");
        Double amount = validator.checkEntryNumber(stringAmount);
        SubscriberReplenishDTO replenishDTO = getSubscriberDTO(request);
        Subscriber returnedSubscriber = subscriberService.replenishBalance(replenishDTO, amount);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + returnedSubscriber.getId());
        HttpSession session = request.getSession();
        session.setAttribute("user", returnedSubscriber);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private SubscriberReplenishDTO getSubscriberDTO(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Subscriber subscriberFromSession = (Subscriber) session.getAttribute("user");
        SubscriberReplenishDTO subscriberReplenishDTO = new SubscriberReplenishDTO();
        subscriberReplenishDTO.setId(subscriberFromSession.getId());
        return subscriberReplenishDTO;

    }
}