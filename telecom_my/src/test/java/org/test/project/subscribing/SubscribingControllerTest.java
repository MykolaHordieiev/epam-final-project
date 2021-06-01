package org.test.project.subscribing;

import org.junit.Before;
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
import org.test.project.rate.dto.RateAddSubscribingDTO;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberService;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscribingControllerTest {

    @Mock
    private SubscribingService subscribingService;
    @Mock
    private SubscriberService subscriberService;
    @Mock
    private RateService rateService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SubscribingController subscribingController;

    private static final Long ID = 1L;
    private Subscriber subscriber = new Subscriber(ID, "login", "aps", 20, false);
    private Rate rate = new Rate(ID, "rate", 10d, ID, false);
    private RateAddSubscribingDTO rateDTO = new RateAddSubscribingDTO(ID, 10d);
    private SubscriberAddSubscribingDTO subscriberDTO = new SubscriberAddSubscribingDTO(ID, 20d);

    @Before
    public void setUp() {
        when(request.getParameter(anyString())).thenReturn("1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(subscriber);
        when(subscriberService.getSubscriberById(ID)).thenReturn(subscriber);
        when(rateService.getRateById(ID)).thenReturn(rate);
    }

    @Test
    public void getRequestMatcher() {
        List<RequestMatcher> requestMatcherList = subscribingController.getRequestMatcher();
        assertNotNull(requestMatcherList);
        assertFalse(requestMatcherList.isEmpty());
        assertEquals(1, requestMatcherList.size());
    }


    @Test
    public void addSubscribingWithoutLock() {
        SubscriberAddSubscribingDTO returnedSubscriberDTO = new SubscriberAddSubscribingDTO(ID, 10d);

        when(subscribingService.addSubscribing(subscriberDTO, ID, rateDTO)).thenReturn(returnedSubscriberDTO);

        ModelAndView modelAndView = subscribingController.addSubscribing(request, response);
        assertEquals("/service/subscriber?id=1", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void addSubscribingWithLock() {
        SubscriberAddSubscribingDTO returnedSubscriberDTO = new SubscriberAddSubscribingDTO(ID, -5d);

        when(subscribingService.addSubscribing(subscriberDTO, ID, rateDTO)).thenReturn(returnedSubscriberDTO);

        ModelAndView modelAndView = subscribingController.addSubscribing(request, response);
        assertNotNull(modelAndView);
        assertEquals("/subscriber/lock.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(subscriberService).lockSubscriberById(ID);
    }

}