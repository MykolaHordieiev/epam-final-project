package org.test.project.subscribing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.rate.dto.RateAddSubscribingDTO;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscribingServiceTest {

    @Mock
    private SubscribingRepository repository;

    @InjectMocks
    private SubscribingService service;

    private static final Long ID = 2L;

    @Test
    public void addSubscribing() {
        RateAddSubscribingDTO rateDTO = new RateAddSubscribingDTO(ID, 20d);
        SubscriberAddSubscribingDTO subscriberDTO = new SubscriberAddSubscribingDTO(ID, 30d);
        SubscriberAddSubscribingDTO expectedSubscriberDTO = new SubscriberAddSubscribingDTO(ID, 10d);

        when(repository.addSubscribing(subscriberDTO, ID, ID)).thenReturn(expectedSubscriberDTO);

        SubscriberAddSubscribingDTO resultSubscriberDTO = service.addSubscribing(subscriberDTO, ID, rateDTO);
        Assert.assertEquals(expectedSubscriberDTO, resultSubscriberDTO);
    }

    @Test
    public void getSubscribing() {
        Subscriber subscriber = new Subscriber(ID, "log", "pas", 2d, false);
        Rate returnedRate = new Rate(ID, "rate", 10d, ID, false);
        Product returnedProduct = new Product(ID, "product");

        Subscribing expectedSubs = new Subscribing(subscriber, returnedProduct, returnedRate);
        List<Subscribing> expectedList = Arrays.asList(expectedSubs);

        when(repository.getSubscribingBySubscriberId(ID)).thenReturn(expectedList);

        List<Subscribing> resultList = service.getSubscribing(ID);
        Assert.assertNotNull(resultList);
        Assert.assertEquals(expectedList, resultList);
    }
}