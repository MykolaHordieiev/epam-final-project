package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;
import org.test.project.infra.web.ModelAndView;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static java.lang.Long.parseLong;

@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;
    private final SubscriberService subscriberService;

    public ModelAndView getAllRates(HttpServletRequest request, HttpServletResponse response) {
        Long productId = parseLong(request.getParameter("productId"));
        List<Rate> rates = rateService.getRatesByProductId(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byproduct.jsp");
        modelAndView.addAttribute("rates", rates);
        modelAndView.addAttribute("productId",productId);
        modelAndView.addAttribute("subscriber",getSubscriberFromSession(request));
        return modelAndView;
    }
    private Subscriber getSubscriberFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if(user.getUserRole().equals(UserRole.SUBSCRIBER)){
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
            String rateName = validatorEntryValue(request.getParameter("name"),"name");
            Double newPrice = Double.parseDouble(validatorEntryValue(request.getParameter("price"),"price"));
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
            throw new RateException("entry parameter cannot be empty: "+ parameter);
        }
        return value;
    }

    public ModelAndView returnViewAddRates(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/add.jsp");
        System.out.println(req.getParameter("id"));
        modelAndView.addAttribute("productId", req.getParameter("id"));
        return modelAndView;
    }

    public ModelAndView addRate(HttpServletRequest request, HttpServletResponse response) {
        String rateName = validatorEntryValue(request.getParameter("name"),"name");
        Double ratePrice = Double.parseDouble(validatorEntryValue(request.getParameter("price"),"price"));
        validatorChanges(rateName, ratePrice);
        Rate rate = new Rate();
        rate.setName(rateName);
        rate.setPrice(ratePrice);
        rate.setProductId(parseLong(request.getParameter("productId")));
        rateService.addRateForProduct(rate);
        return getAllRates(request, response);
    }
}
