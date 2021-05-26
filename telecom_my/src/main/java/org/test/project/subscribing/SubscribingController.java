package org.test.project.subscribing;

import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.product.Product;
import org.test.project.product.ProductService;
import org.test.project.rate.Rate;
import org.test.project.rate.RateService;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class SubscribingController implements Controller {

    private final SubscribingService subscribingService;
    private final SubscriberService subscriberService;
    private final ProductService productService;
    private final RateService rateService;
    private List<RequestMatcher> requestMatchers;

    public SubscribingController(SubscribingService subscribingService, SubscriberService subscriberService,
                                 ProductService productService, RateService rateService) {
        this.subscribingService = subscribingService;
        this.subscriberService = subscriberService;
        this.productService = productService;
        this.rateService = rateService;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/add/subscribing", "POST", this::addSubscribing));
        return requestMatchers;
    }

    public ModelAndView addSubscribing(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.parseLong(request.getParameter("productId"));
        Long rateId = Long.parseLong(request.getParameter("rateId"));
        Subscriber subscriber = getUserOfSession(request);
        Product product = productService.getProductById(productId);
        Rate rate = rateService.getRateById(rateId);
        Subscriber returnedSubscriber = subscribingService.addSubscribing(subscriber, product, rate);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + returnedSubscriber.getId());
        if (returnedSubscriber.getBalance() < 0) {
            subscriberService.lockSubscriberById(returnedSubscriber.getId());
            modelAndView.setView("/subscriber/lock.jsp");
            HttpSession session = request.getSession();
            session.setAttribute("user", returnedSubscriber);
        }
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private Subscriber getUserOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Subscriber) session.getAttribute("user");
    }
}
