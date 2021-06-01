package org.test.project.rate;

import lombok.SneakyThrows;
import org.test.project.infra.web.QueryValueResolver;
import org.test.project.rate.dto.RateAddRequestDTO;
import org.test.project.rate.dto.RateChangeRequestDTO;
import org.test.project.user.User;
import org.test.project.user.UserRole;
import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.product.ProductService;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Predicate;

import static java.lang.Long.parseLong;

public class RateController implements Controller {

    private final RateService rateService;
    private final SubscriberService subscriberService;
    private final ProductService productService;
    private final RateValidator validator;
    private final QueryValueResolver queryValueResolver;
    private List<RequestMatcher> requestMatchers;

    public RateController(RateService rateService, SubscriberService subscriberService, ProductService productService,
                          RateValidator validator, QueryValueResolver queryValueResolver) {
        this.rateService = rateService;
        this.subscriberService = subscriberService;
        this.productService = productService;
        this.validator = validator;
        this.queryValueResolver = queryValueResolver;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/rate/product", "GET", this::getAllRates));
        requestMatchers.add(new RequestMatcher("/rate/info", "GET", this::getRateById));
        requestMatchers.add(new RequestMatcher("/rate", "POST", this::changeRates));
        requestMatchers.add(new RequestMatcher("/rate/add", "GET", this::returnViewAddRates));
        requestMatchers.add(new RequestMatcher("/rate/add", "POST", this::addRate));
        requestMatchers.add(new RequestMatcher("/download/rate", "GET", this::downloadListOfRates));
        return requestMatchers;
    }

    public ModelAndView getAllRates(HttpServletRequest request, HttpServletResponse response) {
        Subscriber subscriberFromSession = getSubscriberFromSession(request);
        Long productId = parseLong(request.getParameter("productId"));
        List<Rate> ratesByProductId = rateService.getRatesByProductId(productId);
        List<Rate> usingRatesBySubscriber = rateService.getRAtesBySubscriberId(subscriberFromSession.getId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byproduct.jsp");
        modelAndView.addAttribute("rates", ratesByProductId);
        modelAndView.addAttribute("productId", productId);
        modelAndView.addAttribute("subscriber", subscriberFromSession);
        modelAndView.addAttribute("ratesMap", getMapWithRates(ratesByProductId, usingRatesBySubscriber));
        return modelAndView;
    }

    private Map<Rate, Boolean> getMapWithRates(List<Rate> ratesByProductId, List<Rate> ratesUsingBySubscriber) {
        Map<Rate, Boolean> ratesMap = new HashMap<>();
        for (Rate rate : ratesByProductId) {
            Boolean check = ratesUsingBySubscriber.stream()
                    .anyMatch(rate1 -> rate.equals(rate1));
            ratesMap.put(rate, check);
        }
        return ratesMap;
    }

    private Subscriber getSubscriberFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user.getUserRole().equals(UserRole.SUBSCRIBER)) {
            Subscriber subscriber = (Subscriber) user;
            return subscriberService.getSubscriberById(subscriber.getId());
        }
        return new Subscriber();
    }

    public ModelAndView getRateById(HttpServletRequest request, HttpServletResponse response) {
        long id = parseLong(request.getParameter("rateId"));
        Rate rate = rateService.getRateById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byid.jsp");
        modelAndView.addAttribute("rate", rate);
        return modelAndView;
    }

    public ModelAndView changeRates(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getParameter("method");
        if (method.equals("PUT")) {
            RateChangeRequestDTO rateDTO = queryValueResolver.getObject(request, RateChangeRequestDTO.class);
            validator.checkEmptyEntryChangeParameter(rateDTO);
            rateService.changeRateById(rateDTO);
            return getAllRates(request, response);
        }
        return deleteRate(request, response);
    }

    public ModelAndView deleteRate(HttpServletRequest request, HttpServletResponse response) {
        long rateId = parseLong(request.getParameter("rateId"));
        Rate rate = rateService.getRateById(rateId);
        List<Subscriber> subscriberList = rateService.checkUsingRateBeforeDelete(rateId);
        if (subscriberList.isEmpty()) {
            rateService.deleteRateById(rateId);
            return getAllRates(request, response);
        }
        rate = rateService.doUnusableRate(rate);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/unusable.jsp");
        modelAndView.addAttribute("rate", rate);
        modelAndView.addAttribute("subscribers", subscriberList);
        return modelAndView;
    }

    public ModelAndView returnViewAddRates(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/add.jsp");
        modelAndView.addAttribute("productId", req.getParameter("productId"));
        return modelAndView;
    }

    public ModelAndView addRate(HttpServletRequest request, HttpServletResponse response) {
        RateAddRequestDTO rateDTO = queryValueResolver.getObject(request, RateAddRequestDTO.class);
        validator.checkEmptyEntryAddParameter(rateDTO);
        rateService.addRateForProduct(rateDTO);
        return getAllRates(request, response);
    }

    @SneakyThrows
    public HttpServletResponse downloadListOfRates(HttpServletRequest request, HttpServletResponse response) {
        Long productId = parseLong(request.getParameter("productId"));
        String productName = productService.getProductById(productId).getName();
        List<Rate> rates = rateService.getRatesByProductId(productId);
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=rates.txt");
        PrintWriter writer = response.getWriter();
        writer.write("Information about rates of product: " + productName);
        writer.write("\n");
        for (Rate rate : rates) {
            writer.write("Name: ");
            writer.write(rate.getName());
            writer.write(" Price: ");
            writer.write(rate.getPrice().toString());
            writer.write("\n");
        }
        writer.close();
        return response;
    }
}
