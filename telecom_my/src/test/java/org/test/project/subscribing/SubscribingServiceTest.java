package org.test.project.subscribing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.Subscriber;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
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
        Subscriber subscriber = new Subscriber();
        subscriber.setBalance(50d);
        Product product = new Product();
        Rate rate = new Rate();
        rate.setPrice(30d);
        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        when(repository.addSubscribing(subscribing)).thenReturn(subscriber);

        Subscriber resultSubscriber = service.addSubscribing(subscriber, product, rate);
        Assert.assertEquals(subscriber, resultSubscriber);
        Assert.assertEquals(20d, resultSubscriber.getBalance(), 0);

        verify(repository, atLeastOnce()).addSubscribing(subscribing);
    }

    @Test
    public void getSubscribing() {
        Subscribing subs1 = new Subscribing(new Subscriber(), new Product(), new Rate());
        Subscribing subs2 = new Subscribing(new Subscriber(), new Product(), new Rate());
        List<Subscribing> expectedList = Arrays.asList(subs1, subs2);
        when(repository.getSubscribingBySubscriberId(ID)).thenReturn(expectedList);

        List<Subscribing> resultList = service.getSubscribing(ID);
        Assert.assertEquals(expectedList, resultList);

        verify(repository, atLeastOnce()).getSubscribingBySubscriberId(ID);
    }
}