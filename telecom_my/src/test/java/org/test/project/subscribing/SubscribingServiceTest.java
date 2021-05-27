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

        verify(repository).addSubscribing(subscribing);
    }

    @Test
    public void getSubscribing() {
        Long ID1 = 3L;
        Subscriber subscriber = new Subscriber();
        Product product = new Product();
        Rate rate = new Rate();
        subscriber.setId(ID);
        product.setId(ID);
        rate.setId(ID);

        Product product1 = new Product();
        Rate rate1 = new Rate();
        product1.setId(ID1);
        rate1.setId(ID1);

        Rate returnedRate = new Rate(ID, "rate", 10d, ID, false);
        Product returnedProduct = new Product(ID, "product");
        Rate returnedRate1 = new Rate(ID1, "rate1", 11d, ID1, false);
        Product returnedProduct1 = new Product(ID1, "product1");

        Subscribing subs = new Subscribing(subscriber, product, rate);
        Subscribing subs1 = new Subscribing(subscriber, product1, rate1);
        List<Subscribing> returnedList = Arrays.asList(subs, subs1);

        Subscribing expectedSubs = new Subscribing(subscriber, returnedProduct, returnedRate);
        Subscribing expectedSubs2 = new Subscribing(subscriber, returnedProduct1, returnedRate1);
        List<Subscribing> expectedList = Arrays.asList(expectedSubs, expectedSubs2);


        when(repository.getSubscribingBySubscriberId(subscriber)).thenReturn(returnedList);

        when(repository.getProduct(product)).thenReturn(returnedProduct);
        when(repository.getRateBy(rate)).thenReturn(returnedRate);

        when(repository.getProduct(product1)).thenReturn(returnedProduct1);
        when(repository.getRateBy(rate1)).thenReturn(returnedRate1);

        List<Subscribing> resultList = service.getSubscribing(subscriber);
        Assert.assertNotNull(resultList);
        Assert.assertEquals(expectedList, resultList);

        verify(repository).getSubscribingBySubscriberId(subscriber);
        verify(repository).getProduct(product);
        verify(repository).getRateBy(rate);
        verify(repository).getProduct(product1);
        verify(repository).getRateBy(rate1);
    }
}