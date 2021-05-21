package org.test.project.rate;

import lombok.SneakyThrows;
import org.test.project.User.User;
import org.test.project.User.UserRole;
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
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class RateController implements Controller {

    private final RateService rateService;
    private final SubscriberService subscriberService;
    private final ProductService productService;
    private List<RequestMatcher> requestMatchers;

    public RateController(RateService rateService, SubscriberService subscriberService, ProductService productService) {
        this.rateService = rateService;
        this.subscriberService = subscriberService;
        this.productService = productService;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/rate/product", "GET", this::getAllRates));
        requestMatchers.add(new RequestMatcher("/rate/info", "GET", this::getRateById));
        requestMatchers.add(new RequestMatcher("/rate", "POST", this::changeRates));
        requestMatchers.add(new RequestMatcher("/rate/add", "GET", this::returnViewAddRates));
        requestMatchers.add(new RequestMatcher("/rate/add", "POST", this::addRate));
        requestMatchers.add(new RequestMatcher("/rate","GET",this::downloadListOfRates));
        return requestMatchers;
    }

    public ModelAndView getAllRates(HttpServletRequest request, HttpServletResponse response) {
        Long productId = parseLong(request.getParameter("productId"));
        List<Rate> rates = rateService.getRatesByProductId(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byproduct.jsp");
        modelAndView.addAttribute("rates", rates);
        modelAndView.addAttribute("productId", productId);
        modelAndView.addAttribute("subscriber", getSubscriberFromSession(request));
        return modelAndView;
    }

    private Subscriber getSubscriberFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user.getUserRole().equals(UserRole.SUBSCRIBER)) {
            return subscriberService.getSubscriberById(user.getId());
        }
        return new Subscriber();
    }

    public ModelAndView getRateById(HttpServletRequest request, HttpServletResponse response) {
        long id = parseLong(request.getParameter("id"));
        Rate rate = rateService.getRateById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byid.jsp");
        modelAndView.addAttribute("rate", rate);
        return modelAndView;
    }

    public ModelAndView changeRates(HttpServletRequest request, HttpServletResponse response) {
        Rate rate = new Rate();
        String method = request.getParameter("method");
        if (method.equals("PUT")) {
            String rateName = validatorEntryValue(request.getParameter("name"), "name");
            Double newPrice = Double.parseDouble(validatorEntryValue(request.getParameter("price"), "price"));
            Long id = Long.parseLong(request.getParameter("id"));
            validatorChanges(rateName, newPrice);
            rate.setId(id);
            rate.setName(rateName);
            rate.setPrice(newPrice);
            rateService.changeRateById(rate);
        } else {
            Long rateId = parseLong(request.getParameter("id"));
            rate.setId(rateId);
            rateService.checkUsingRateBeforeDelete(rate);
            rateService.deleteRateById(rate);
        }
        return getAllRates(request, response);
    }

    private boolean validatorChanges(String rateName, Double newPrice) {
        if (rateName.equals("") || newPrice < 0) {
            throw new RateException("price cannot be < 0, name of Rate cannot be empty");
        }
        return true;
    }

    private String validatorEntryValue(String value, String parameter) {
        if (value.equals("")) {
            throw new RateException("entry parameter cannot be empty: " + parameter);
        }
        return value;
    }

    public ModelAndView returnViewAddRates(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/add.jsp");
        modelAndView.addAttribute("productId", req.getParameter("id"));
        return modelAndView;
    }

    public ModelAndView addRate(HttpServletRequest request, HttpServletResponse response) {
        String rateName = validatorEntryValue(request.getParameter("name"), "name");
        Double ratePrice = Double.parseDouble(validatorEntryValue(request.getParameter("price"), "price"));
        validatorChanges(rateName, ratePrice);
        Rate rate = new Rate();
        rate.setName(rateName);
        rate.setPrice(ratePrice);
        rate.setProductId(parseLong(request.getParameter("productId")));
        rateService.addRateForProduct(rate);
        return getAllRates(request, response);
    }

    @SneakyThrows
    public HttpServletResponse downloadListOfRates(HttpServletRequest request, HttpServletResponse response) {
        String format = request.getParameter("format");
        Long productId = parseLong(request.getParameter("productId"));
        String productName = productService.getProductById(productId).getName();
        List<Rate> rates = rateService.getRatesByProductId(productId);
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=rates" + format);
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
