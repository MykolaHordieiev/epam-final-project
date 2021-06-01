package org.test.project.product;

import org.test.project.infra.web.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ProductController implements Controller {

    private final ProductService productService;
    private List<RequestMatcher> requestMatchers;

    public ProductController(ProductService productService) {
        this.productService = productService;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/get/all/product", "GET", this::getAllProducts));
        return requestMatchers;
    }

    public ModelAndView getAllProducts(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/product/all.jsp");
        modelAndView.addAttribute("products", productService.getAllProduct());
        return modelAndView;
    }
}
