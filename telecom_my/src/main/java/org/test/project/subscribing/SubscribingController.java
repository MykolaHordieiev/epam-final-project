package org.test.project.subscribing;

import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.rate.Rate;
import org.test.project.rate.dto.RateAddSubscribingDTO;
import org.test.project.rate.RateService;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class SubscribingController implements Controller {

    private final SubscribingService subscribingService;
    private final SubscriberService subscriberService;
    private final RateService rateService;
    private List<RequestMatcher> requestMatchers;

    public SubscribingController(SubscribingService subscribingService, SubscriberService subscriberService,
                                 RateService rateService) {
        this.subscribingService = subscribingService;
        this.subscriberService = subscriberService;
        this.rateService = rateService;
        requestMatchers = new ArrayList<>();
        requestMatchers.add(new RequestMatcher("/add/subscribing", "POST", this::addSubscribing));
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        return requestMatchers;
    }

    public ModelAndView addSubscribing(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.parseLong(request.getParameter("productId"));
        Long rateId = Long.parseLong(request.getParameter("rateId"));
        SubscriberAddSubscribingDTO subscriberDTO = getSubscriberDTO(request);
        RateAddSubscribingDTO rateDTO = getRateDTO(rateId);
        SubscriberAddSubscribingDTO returnedSubscriberDTO = subscribingService.addSubscribing(subscriberDTO, productId, rateDTO);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + returnedSubscriberDTO.getId());
        if (returnedSubscriberDTO.getBalance() < 0) {
            subscriberService.lockSubscriberById(returnedSubscriberDTO.getId());
            modelAndView.setView("/subscriber/lock.jsp");
        }
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private RateAddSubscribingDTO getRateDTO(Long rateId) {
        Rate rate = rateService.getRateById(rateId);
        return new RateAddSubscribingDTO(rateId, rate.getPrice());
    }

    private SubscriberAddSubscribingDTO getSubscriberDTO(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Subscriber subscriberFromSession = (Subscriber) session.getAttribute("user");
        Subscriber subscriberById = subscriberService.getSubscriberById(subscriberFromSession.getId());
        return new SubscriberAddSubscribingDTO(subscriberById.getId(), subscriberById.getBalance());

    }
}
