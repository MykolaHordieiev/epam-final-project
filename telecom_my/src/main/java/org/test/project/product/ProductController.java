package org.test.project.product;

import lombok.RequiredArgsConstructor;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    public ModelAndView getAllProducts(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/product/all.jsp");
        modelAndView.addAttribute("products", productService.getAllProduct());
        return modelAndView;
    }
}
