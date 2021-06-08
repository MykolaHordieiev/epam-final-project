package org.test.project.subscriber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.QueryValueResolver;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;
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
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private QueryValueResolver queryValueResolver;
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
        when(subscriberService.getSubscriberById(ID)).thenReturn(expectedSubscriber);
        when(subscribingService.getSubscribing(ID)).thenReturn(subscribingList);

        ModelAndView modelAndView = subscriberController.getSubscriberById(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertEquals(expectedSubscriber, modelAndView.getAttributes().get("subscriber"));
        assertEquals(subscribingList, modelAndView.getAttributes().get("subscriptions"));
    }

    @Test
    public void getSubscriberByLogin() {
        Subscriber foundSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 0, false);
        Subscribing subscribing1 = new Subscribing(foundSubscriber, new Product(ID, "product"), new Rate());
        Subscribing subscribing2 = new Subscribing(foundSubscriber, new Product(2L, "BBC"),
                new Rate(ID, "super", 40d, 2L, false));
        List<Subscribing> subscribingList = Arrays.asList(subscribing1, subscribing2);

        when(request.getParameter("login")).thenReturn(LOGIN);
        when(validator.checkEmptyLogin(LOGIN)).thenReturn(LOGIN);
        when(subscriberService.getSubscriberByLogin(LOGIN)).thenReturn(foundSubscriber);
        when(subscribingService.getSubscribing(ID)).thenReturn(subscribingList);

        ModelAndView modelAndView = subscriberController.getSubscriberByLogin(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertEquals(foundSubscriber, modelAndView.getAttributes().get("subscriber"));
        assertEquals(subscribingList, modelAndView.getAttributes().get("subscriptions"));
    }

    @Test
    public void createSubscriber() {
        SubscriberCreateDTO dto = new SubscriberCreateDTO(ID, LOGIN, PASSWORD);
        Subscriber afterCreateSubscriber = new Subscriber();
        afterCreateSubscriber.setId(ID);

        when(queryValueResolver.getObject(request, SubscriberCreateDTO.class)).thenReturn(dto);
        when(validator.checkValidLoginPassword(dto)).thenReturn(dto);
        when(subscriberService.create(dto)).thenReturn(afterCreateSubscriber);

        ModelAndView modelAndView = subscriberController.createSubscriber(request, response);
        assertNotNull(modelAndView);
        assertEquals("/service/subscriber?id=1", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void getAll() {
        double countOfHref = 1.0;
        List<Subscriber> expectedList = Arrays.asList(new Subscriber(ID, LOGIN, PASSWORD, 0, false),
                new Subscriber(2L, "lo", "pa", 10d, true));

        when(request.getParameter("page")).thenReturn("1");
        when(subscriberService.getAll(anyInt())).thenReturn(expectedList);
        when(subscriberService.getCountOfHref()).thenReturn(countOfHref);

        ModelAndView modelAndView = subscriberController.getAll(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/all.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("subscribers"));
        assertEquals(countOfHref,modelAndView.getAttributes().get("countOfHref"));

        verify(subscriberService).getAll(anyInt());
    }

    @Test
    public void lockSubscriber() {
        Subscriber lockSubscriber = new Subscriber();
        lockSubscriber.setId(ID);
        lockSubscriber.setLock(true);

        when(request.getParameter("id")).thenReturn("1");
        when(subscriberService.lockSubscriberById(ID)).thenReturn(lockSubscriber);

        ModelAndView modelAndView = subscriberController.lockSubscriber(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(2)).getParameter("id");
        verify(subscriberService).lockSubscriberById(ID);
        verify(subscriberService).getSubscriberById(anyLong());
        verify(subscribingService).getSubscribing(anyLong());
    }

    @Test
    public void unlockSubscriber() {
        Subscriber unlockSubscriber = new Subscriber();
        unlockSubscriber.setId(ID);
        unlockSubscriber.setLock(false);

        when(request.getParameter("id")).thenReturn("1");
        when(subscriberService.unlockSubscriberById(ID)).thenReturn(unlockSubscriber);

        ModelAndView modelAndView = subscriberController.unlockSubscriber(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/infobyid.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());

        verify(request, times(2)).getParameter("id");
        verify(subscriberService).unlockSubscriberById(ID);
        verify(subscriberService).getSubscriberById(anyLong());
        verify(subscribingService).getSubscribing(anyLong());
    }

    @Test
    public void replenishBalance() {
        double balance = 20d;
        Subscriber subscriber = new Subscriber(ID, LOGIN, PASSWORD, balance, false);
        String amountStr = "10";
        double amount = 10d;
        SubscriberReplenishDTO subscriberDTO = new SubscriberReplenishDTO();
        subscriberDTO.setId(ID);
        Subscriber returnedSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 30d, false);

        when(request.getParameter("amount")).thenReturn(amountStr);
        when(validator.checkEntryNumber(amountStr)).thenReturn(amount);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(subscriber);
        when(subscriberService.replenishBalance(subscriberDTO, amount)).thenReturn(returnedSubscriber);
        when(request.getSession()).thenReturn(session);

        ModelAndView modelAndView = subscriberController.replenishBalance(request, response);
        assertNotNull(modelAndView);
        assertEquals("/service/subscriber?id=1", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(session).setAttribute("user", returnedSubscriber);
    }
}