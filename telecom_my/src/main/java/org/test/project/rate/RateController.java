package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import org.test.project.entity.Rate;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static java.lang.Long.parseLong;

@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    public ModelAndView getAllRates(HttpServletRequest request, HttpServletResponse response) {
        Long productId = parseLong(request.getParameter("id"));
        List<Rate> rates = rateService.getRatesByProductId(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byproduct.jsp");
        modelAndView.addAttribute("rates", rates);
        return modelAndView;
    }

    public ModelAndView getRateById(HttpServletRequest request, HttpServletResponse response) {
        long id = parseLong(request.getParameter("id"));
        Rate rate = rateService.getRateById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/rate/byid.jsp");
        modelAndView.addAttribute("rate", rate);
        return modelAndView;
    }
}
