package org.test.project.subscribing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscribingControllerTest {

    @Mock
    private SubscribingService subscribingService;
    @Mock
    private SubscriberService subscriberService;
    @Mock
    private ProductService productService;
    @Mock
    private RateService rateService;

    @Mock
    private HttpServletRequest request;


    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SubscribingController subscribingController;

    private Subscriber subscriber = new Subscriber(2L, "login", "aps", 0, false);
    private Product product = new Product(1L, "product");
    private Rate rate = new Rate(1L, "rate", 10d, 1L, false);

    @Test
    public void getRequestMatcher() {
        List<RequestMatcher> requestMatcherList = subscribingController.getRequestMatcher();
        assertNotNull(requestMatcherList);
        assertFalse(requestMatcherList.isEmpty());
        assertEquals(1, requestMatcherList.size());
    }

    @Test
    public void addSubscribingWithoutLock() {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rateId")).thenReturn("1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(subscriber);
        when(productService.getProductById(1L)).thenReturn(product);
        when(rateService.getRateById(1L)).thenReturn(rate);
        when(subscribingService.addSubscribing(subscriber, product, rate)).thenReturn(subscriber);

        ModelAndView modelAndView = subscribingController.addSubscribing(request, response);
        assertEquals("/service/subscriber?id=2", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getParameter("productId");
        verify(request).getParameter("rateId");
        verify(request).getSession(false);
        verify(session).getAttribute("user");
        verify(productService).getProductById(1L);
        verify(rateService).getRateById(1L);
        verify(subscribingService).addSubscribing(subscriber, product, rate);
    }

    @Test
    public void addSubscribingWithLock() {
        Subscriber returnedSub = new Subscriber(2L, "login", "aps", -40, false);
        Subscriber returnedLockSub = new Subscriber(2L, "login", "aps", -40, true);

        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rateId")).thenReturn("1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(subscriber);
        when(productService.getProductById(1L)).thenReturn(product);
        when(rateService.getRateById(1L)).thenReturn(rate);
        when(subscribingService.addSubscribing(subscriber, product, rate)).thenReturn(returnedSub);
        when(subscriberService.lockSubscriberById(returnedSub)).thenReturn(returnedLockSub);
        when(request.getSession()).thenReturn(session);

        ModelAndView modelAndView = subscribingController.addSubscribing(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/lock.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getParameter("productId");
        verify(request).getParameter("rateId");
        verify(request).getSession(false);
        verify(session).getAttribute("user");
        verify(productService).getProductById(1L);
        verify(rateService).getRateById(1L);
        verify(subscribingService).addSubscribing(subscriber, product, rate);
        verify(request).getSession();
        verify(session).setAttribute("user",returnedLockSub);
    }

}