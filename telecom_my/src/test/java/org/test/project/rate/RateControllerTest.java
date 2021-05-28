package org.test.project.rate;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.product.Product;
import org.test.project.product.ProductService;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RateControllerTest {

    @Mock
    private RateService rateService;
    @Mock
    private SubscriberService subscriberService;
    @Mock
    private ProductService productService;
    @Mock
    private RateValidator validator;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private PrintWriter writer;
    @InjectMocks
    private RateController rateController;

    private static final Long ID = 1L;

    @Test
    public void getRequestMatcher() {
            List<RequestMatcher> requestMatcherList = rateController.getRequestMatcher();
            assertNotNull(requestMatcherList);
            assertFalse(requestMatcherList.isEmpty());
            assertEquals(6, requestMatcherList.size());
    }

    @Test
    public void getAllRates() {
        Subscriber subscriberFromSession = new Subscriber();
        subscriberFromSession.setId(ID);
        Subscriber returnedSubscriber = new Subscriber(ID, "log", "pass", 0, false);
        Rate rate1 = new Rate(ID, "100 min", 10d, ID, false);
        Rate rate2 = new Rate(2L, "200 min", 20d, ID, false);
        List<Rate> expectedList = Arrays.asList(rate1, rate2);

        when(request.getParameter("productId")).thenReturn("1");
        when(rateService.getRatesByProductId(ID)).thenReturn(expectedList);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(subscriberFromSession);
        when(subscriberService.getSubscriberById(subscriberFromSession)).thenReturn(returnedSubscriber);

        ModelAndView modelAndView = rateController.getAllRates(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/byproduct.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());
        assertEquals(returnedSubscriber, modelAndView.getAttributes().get("subscriber"));
        assertEquals(expectedList, modelAndView.getAttributes().get("rates"));
        assertEquals(ID, modelAndView.getAttributes().get("productId"));

        verify(request).getParameter("productId");
        verify(request).getSession(false);
        verify(rateService).getRatesByProductId(ID);
        verify(session).getAttribute("user");
        verify(subscriberService).getSubscriberById(subscriberFromSession);
    }

    @Test
    public void getRateById() {
        Rate rate = new Rate(ID, "100 min", 10d, ID, false);

        when(request.getParameter("id")).thenReturn("1");
        when(rateService.getRateById(ID)).thenReturn(rate);

        ModelAndView modelAndView = rateController.getRateById(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/byid.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());
        assertEquals(rate, modelAndView.getAttributes().get("rate"));

        verify(request).getParameter("id");
        verify(rateService).getRateById(ID);
    }

    @Test
    public void changeRates() {
        String name = "super";
        String price = "100";
        Rate rate = new Rate(ID, name, 100d, ID, false);

        when(request.getParameter("method")).thenReturn("PUT");
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(price);
        when(request.getParameter("id")).thenReturn("1");
        when(validator.checkEmptyEntryParameter(name, price)).thenReturn(rate);
        when(rateService.changeRateById(rate)).thenReturn(rate);
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Subscriber());

        ModelAndView modelAndView = rateController.changeRates(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/byproduct.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(5)).getParameter(anyString());
        verify(validator).checkEmptyEntryParameter(name, price);
        verify(rateService).changeRateById(rate);
        verify(request).getSession(false);
        verify(rateService).getRatesByProductId(anyLong());
        verify(session).getAttribute("user");
        verify(subscriberService).getSubscriberById(any(Subscriber.class));
    }

    @Test
    public void deleteRateIsUsingBySubscriber() {
        Subscriber subscriber = new Subscriber();
        Subscriber subscriberWithLogin = new Subscriber();
        subscriberWithLogin.setLogin("login");
        Rate rate = new Rate(ID, "super", 100d, ID, false);
        Rate unusableRate = new Rate(ID, "super", 100d, ID, true);
        List<Subscriber> subscriberList = Arrays.asList(subscriber);
        when(request.getParameter("method")).thenReturn("DELETE");
        when(request.getParameter("id")).thenReturn("1");
        when(rateService.getRateById(ID)).thenReturn(rate);
        when(rateService.checkUsingRateBeforeDelete(rate)).thenReturn(subscriberList);
        when(subscriberService.getSubscriberById(subscriber)).thenReturn(subscriberWithLogin);
        when(rateService.doUnusableRate(rate)).thenReturn(unusableRate);

        ModelAndView modelAndView = rateController.changeRates(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/unusable.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());
        assertEquals(unusableRate, modelAndView.getAttributes().get("rate"));
        assertEquals(subscriberList, modelAndView.getAttributes().get("subscribers"));

        verify(request, times(2)).getParameter(anyString());
        verify(rateService).getRateById(ID);
        verify(rateService).checkUsingRateBeforeDelete(rate);
        verify(subscriberService).getSubscriberById(subscriber);
        verify(rateService).doUnusableRate(rate);
    }

    @Test
    public void deleteRateWhenRateIsNotUsingBySubscriber() {
        Rate rate = new Rate(ID, "super", 100d, ID, false);
        List<Subscriber> subscriberList = Collections.emptyList();

        when(request.getParameter("method")).thenReturn("DELETE");
        when(request.getParameter("id")).thenReturn("1");
        when(rateService.getRateById(ID)).thenReturn(rate);
        when(rateService.checkUsingRateBeforeDelete(rate)).thenReturn(subscriberList);
        when(rateService.deleteRateById(rate)).thenReturn(rate);

        when(request.getParameter("productId")).thenReturn("1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Subscriber());

        ModelAndView modelAndView = rateController.changeRates(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/byproduct.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(3)).getParameter(anyString());
        verify(rateService).deleteRateById(rate);
        verify(rateService).getRateById(ID);
        verify(rateService).checkUsingRateBeforeDelete(rate);

        verify(request).getSession(false);
        verify(rateService).getRatesByProductId(anyLong());
        verify(session).getAttribute("user");
        verify(subscriberService).getSubscriberById(any(Subscriber.class));
    }

    @Test
    public void returnViewAddRates() {
        when(request.getParameter("id")).thenReturn("1");

        ModelAndView modelAndView = rateController.returnViewAddRates(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/add.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());
        assertEquals(ID.toString(), modelAndView.getAttributes().get("productId"));

        verify(request).getParameter("id");
    }

    @Test
    public void addRate() {
        String name = "super";
        String price = "100";
        Rate rate = new Rate(ID, name, 100d, ID, false);

        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(price);
        when(request.getParameter("productId")).thenReturn("1");
        when(validator.checkEmptyEntryParameter(name, price)).thenReturn(rate);
        when(rateService.addRateForProduct(rate)).thenReturn(rate);

        when(request.getParameter("productId")).thenReturn("1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Subscriber());

        ModelAndView modelAndView = rateController.addRate(request, response);
        assertNotNull(modelAndView);
        assertEquals("/rate/byproduct.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(4)).getParameter(anyString());
        verify(validator).checkEmptyEntryParameter(name, price);
        verify(rateService).addRateForProduct(rate);

        verify(request).getSession(false);
        verify(rateService).getRatesByProductId(anyLong());
        verify(session).getAttribute("user");
        verify(subscriberService).getSubscriberById(any(Subscriber.class));
    }

    @SneakyThrows
    @Test
    public void downloadListOfRates() {
        Rate rate1 = new Rate(ID, "100 min", 10d, ID, false);
        Rate rate2 = new Rate(2L, "200 min", 20d, ID, false);
        List<Rate> expectedList = Arrays.asList(rate1, rate2);

        when(request.getParameter("productId")).thenReturn("1");
        when(productService.getProductById(ID)).thenReturn(new Product());
        when(rateService.getRatesByProductId(ID)).thenReturn(expectedList);
        when(response.getWriter()).thenReturn(writer);

        HttpServletResponse returnedResponse = rateController.downloadListOfRates(request, response);
        assertNotNull(returnedResponse);
        verify(response).setContentType(anyString());
        verify(response).setHeader(anyString(),anyString());
        verify(response).getWriter();
        verify(writer,atLeastOnce()).write(anyString());
        verify(writer).close();
        verify(request).getParameter("productId");
        verify(productService).getProductById(ID);
        verify(rateService).getRatesByProductId(ID);
    }
}