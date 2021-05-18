package org.test.project.operator;

import lombok.RequiredArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;



    public ModelAndView deleteRate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        operatorService.deleteRateForProduct(Long.parseLong(id));
        return getAllProductsWithRate(request, response);
    }

    public ModelAndView changeRate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id.equals("")) {
            throw new FiledChangeRate("please, entry the id");
        }
        String price = request.getParameter("price");
        if (price.equals("")) {
            price += 0;
        }
        String name = request.getParameter("name");
        operatorService.doChangeRateById(Long.parseLong(id), name, Double.parseDouble(price));
        return getAllProductsWithRate(request, response);
    }

    public ModelAndView getAllProductsWithRate(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/product/all.jsp");
        modelAndView.addAttribute("rates", operatorService.getAllRates());
        System.out.println(operatorService.getAllProduct());
        modelAndView.addAttribute("products", operatorService.getAllProduct());
        return modelAndView;
    }


}
