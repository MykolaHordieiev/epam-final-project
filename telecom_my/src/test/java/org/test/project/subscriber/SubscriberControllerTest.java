package org.test.project.subscriber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscribing.Subscribing;
import org.test.project.subscribing.SubscribingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberControllerTest {

    @Mock
    private SubscriberService subscriberService;
    @Mock
    private SubscribingService subscribingService;
    @Mock
    private SubscriberValidator validator;
    @Mock
    HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    HttpSession session;
    @InjectMocks
    private SubscriberController subscriberController;

    private static final Long ID = 1L;
    private static final String LOGIN = "login1";
    private static final String PASSWORD = "pass";

    @Test
    public void getSubscriberById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber expectedSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 0, false);
        Subscribing subscribing1 = new Subscribing(subscriber, new Product(ID, "product"), new Rate());
        Subscribing subscribing2 = new Subscribing(subscriber, new Product(2L, "BBC"),
                new Rate(ID, "super", 40d, 2L, false));
        List<Subscribing> subscribingList = Arrays.asList(subscribing1, subscribing2);

        when(request.getParameter("id")).thenReturn("1");
        when(subscriberService.getSubscriberById(subscriber)).thenReturn(expectedSubscriber);
        when(subscribingService.getSubscribing(subscriber)).thenReturn(subscribingList);

        ModelAndView modelAndView = subscriberController.getSubscriberById(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertEquals(expectedSubscriber, modelAndView.getAttributes().get("subscriber"));
        assertEquals(subscribingList, modelAndView.getAttributes().get("subscriptions"));

        verify(request).getParameter("id");
        verify(subscriberService).getSubscriberById(subscriber);
        verify(subscribingService).getSubscribing(subscriber);
    }

    @Test
    public void getSubscriberByLogin() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(LOGIN);
        Subscriber foundSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 0, false);
        Subscribing subscribing1 = new Subscribing(foundSubscriber, new Product(ID, "product"), new Rate());
        Subscribing subscribing2 = new Subscribing(foundSubscriber, new Product(2L, "BBC"),
                new Rate(ID, "super", 40d, 2L, false));
        List<Subscribing> subscribingList = Arrays.asList(subscribing1, subscribing2);

        when(request.getParameter("login")).thenReturn(LOGIN);
        when(validator.checkEmptyLogin(subscriber)).thenReturn(subscriber);
        when(subscriberService.getSubscriberByLogin(subscriber)).thenReturn(foundSubscriber);
        when(subscribingService.getSubscribing(foundSubscriber)).thenReturn(subscribingList);

        ModelAndView modelAndView = subscriberController.getSubscriberByLogin(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertEquals(foundSubscriber, modelAndView.getAttributes().get("subscriber"));
        assertEquals(subscribingList, modelAndView.getAttributes().get("subscriptions"));

        verify(request).getParameter("login");
        verify(validator).checkEmptyLogin(subscriber);
        verify(subscriberService).getSubscriberByLogin(subscriber);
        verify(subscribingService).getSubscribing(foundSubscriber);
    }

    @Test
    public void createSubscriber() {
        Subscriber beforeCreateSubscriber = new Subscriber();
        beforeCreateSubscriber.setLogin(LOGIN);
        beforeCreateSubscriber.setPassword(PASSWORD);
        Subscriber afterCreateSubscriber = new Subscriber();
        afterCreateSubscriber.setId(ID);

        when(request.getParameter("login")).thenReturn(LOGIN);
        when(request.getParameter("password")).thenReturn(PASSWORD);
        when(validator.checkValidLoginPassword(beforeCreateSubscriber)).thenReturn(beforeCreateSubscriber);
        when(subscriberService.create(beforeCreateSubscriber)).thenReturn(afterCreateSubscriber);

        ModelAndView modelAndView = subscriberController.createSubscriber(request, response);
        assertNotNull(modelAndView);
        assertEquals("/service/subscriber?id=1", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getParameter("login");
        verify(request).getParameter("password");
        verify(validator).checkValidLoginPassword(beforeCreateSubscriber);
        verify(subscriberService).create(beforeCreateSubscriber);
    }

    @Test
    public void getAll() {
        List<Subscriber> expectedList = Arrays.asList(new Subscriber(ID, LOGIN, PASSWORD, 0, false),
                new Subscriber(2L, "lo", "pa", 10d, true));

        when(subscriberService.getAll()).thenReturn(expectedList);

        ModelAndView modelAndView = subscriberController.getAll(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/all.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("subscribers"));

        verify(subscriberService).getAll();
    }

    @Test
    public void lockSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber lockSubscriber = new Subscriber();
        lockSubscriber.setId(ID);
        lockSubscriber.setLock(true);

        when(request.getParameter("id")).thenReturn("1");
        when(subscriberService.lockSubscriberById(subscriber)).thenReturn(lockSubscriber);

        ModelAndView modelAndView = subscriberController.lockSubscriber(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(2)).getParameter("id");
        verify(subscriberService).lockSubscriberById(subscriber);
        verify(subscriberService).getSubscriberById(any(Subscriber.class));
        verify(subscribingService).getSubscribing(any(Subscriber.class));
    }

    @Test
    public void unlockSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber unlockSubscriber = new Subscriber();
        unlockSubscriber.setId(ID);
        unlockSubscriber.setLock(false);

        when(request.getParameter("id")).thenReturn("1");
        when(subscriberService.unlockSubscriberById(subscriber)).thenReturn(unlockSubscriber);

        ModelAndView modelAndView = subscriberController.unlockSubscriber(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(2)).getParameter("id");
        verify(subscriberService).unlockSubscriberById(subscriber);
        verify(subscriberService).getSubscriberById(any(Subscriber.class));
        verify(subscribingService).getSubscribing(any(Subscriber.class));
    }

    @Test
    public void topUpTheBalance() {
        Subscriber subscriber = new Subscriber(ID, LOGIN, PASSWORD, 0, false);
        String amountStr = "10";
        double amount = 10d;
        Subscriber returnedSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 10d, false);

        when(request.getParameter("amount")).thenReturn(amountStr);
        when(validator.checkEntryNumber(amountStr)).thenReturn(amount);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(subscriber);
        when(subscriberService.topUpBalance(subscriber, amount)).thenReturn(returnedSubscriber);
        when(request.getSession()).thenReturn(session);


        ModelAndView modelAndView = subscriberController.topUpTheBalance(request, response);
        assertNotNull(modelAndView);
        assertEquals("/service/subscriber?id=1", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getParameter("amount");
        verify(request).getSession(false);
        verify(request).getSession();
        verify(validator).checkEntryNumber("10");
        verify(session).setAttribute("user", returnedSubscriber);
        verify(session).getAttribute("user");
        verify(subscriberService).topUpBalance(subscriber, amount);
    }
}