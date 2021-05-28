package org.test.project.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private ProductController productController;

    @Test
    public void getRequestMatcher() {
        List<RequestMatcher> list = productController.getRequestMatcher();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    public void getAllProducts() {
        Product internet = new Product(1L, "Internet");
        Product mobile = new Product(2L, "mobile");
        List<Product> products = Arrays.asList(internet, mobile);

        when(productService.getAllProduct()).thenReturn(products);

        ModelAndView modelAndView = productController.getAllProducts(request, response);
        assertNotNull(modelAndView);
        assertFalse(modelAndView.isRedirect());
        assertEquals("/product/all.jsp", modelAndView.getView());
        assertEquals(products, modelAndView.getAttributes().get("products"));

        verify(productService).getAllProduct();
    }
}