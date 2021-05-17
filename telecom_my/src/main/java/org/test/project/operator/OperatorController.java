package org.test.project.operator;

import lombok.RequiredArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;
import org.test.project.entity.Product;
import org.test.project.entity.Rate;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;

    public ModelAndView addRate(HttpServletRequest request, HttpServletResponse response) {
        String nameOfRate = request.getParameter("name");
        String price = request.getParameter("price");
        String idOfProduct = request.getParameter("id");
        operatorService.addRateForProduct(nameOfRate, Double.parseDouble(price), Long.parseLong(idOfProduct));
        return getAllProductsWithRate(request, response);
    }

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
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user.getUserRole().equals(UserRole.SUBSCRIBER)) {
            modelAndView.setView("/subscriber/getallentity.jsp");
        } else {
            modelAndView.setView("/operator/getallentity.jsp");
        }
        modelAndView.addAttribute("rates", operatorService.getAllRates());
        System.out.println(operatorService.getAllProduct());
        modelAndView.addAttribute("products", operatorService.getAllProduct());
        return modelAndView;
    }


}
