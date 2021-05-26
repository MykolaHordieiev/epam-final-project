package org.test.project.rate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.subscriber.Subscriber;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {

    @Mock
    private RateRepository rateRepository;

    @InjectMocks
    private RateService rateService;
    private static final Long ID = 1L;

    @Test
    public void getRatesByProductId() {
        List<Rate> expectedRateList = Arrays.asList(new Rate(), new Rate());
        when(rateRepository.getRatesByProduct(ID)).thenReturn(expectedRateList);

        List<Rate> resultRateList = rateService.getRatesByProductId(ID);
        Assert.assertEquals(expectedRateList, resultRateList);

        verify(rateRepository, atLeastOnce()).getRatesByProduct(ID);
    }

    @Test
    public void getRateByIdWhenRepositoryReturnRate() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);

        when(rateRepository.getRateById(ID)).thenReturn(Optional.of(rate));
        Rate resultRate = rateService.getRateById(ID);
        Assert.assertEquals(rate, resultRate);

        verify(rateRepository, atLeastOnce()).getRateById(ID);
    }

    @Test(expected = RateException.class)
    public void getRateByIdWhenRepositoryReturnEmptyOptional() {
        when(rateRepository.getRateById(ID)).thenReturn(Optional.empty());
        rateService.getRateById(ID);
        verify(rateRepository, atLeastOnce()).getRateById(ID);
    }

    @Test
    public void changeRateById() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);
        when(rateRepository.changeRateById(rate)).thenReturn(rate);

        Rate resultRate = rateService.changeRateById(rate);
        Assert.assertEquals(rate, resultRate);

        verify(rateRepository, atLeastOnce()).changeRateById(rate);
    }

    @Test
    public void addRateForProductWhenRepositoryReturnRate() {
        Rate rate = new Rate();
        rate.setName("100+chanel");
        rate.setPrice(15d);
        rate.setProductId(ID);
        Rate returnedRate = new Rate();
        returnedRate.setId(ID);
        when(rateRepository.addRateByProductId(rate)).thenReturn(Optional.of(rate));

        Rate resultRate = rateService.addRateForProduct(rate);
        Assert.assertEquals(rate, resultRate);

        verify(rateRepository, atLeastOnce()).addRateByProductId(rate);
    }

    @Test(expected = RateException.class)
    public void addRateForProductWhenRepositoryReturnEmptyOptional() {
        Rate rate = new Rate();
        rate.setName("100+chanel");
        rate.setPrice(15d);
        rate.setProductId(ID);
        when(rateRepository.addRateByProductId(rate)).thenReturn(Optional.empty());
        rateService.addRateForProduct(rate);

        verify(rateRepository, atLeastOnce()).addRateByProductId(rate);
    }

    @Test
    public void deleteRateById() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);
        when(rateRepository.deleteRateById(rate)).thenReturn(rate);

        Rate resultRate = rateService.deleteRateById(rate);
        Assert.assertEquals(rate, resultRate);

        verify(rateRepository, atLeastOnce()).deleteRateById(rate);
    }

    @Test
    public void checkUsingRateBeforeDelete() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);
        List<Subscriber> expectedSubscriberList = Arrays.asList(new Subscriber(), new Subscriber());
        when(rateRepository.checkUsingRateBySubscribers(rate)).thenReturn(expectedSubscriberList);

        List<Subscriber> resultSubscriberList = rateService.checkUsingRateBeforeDelete(rate);
        Assert.assertEquals(expectedSubscriberList, resultSubscriberList);

        verify(rateRepository, atLeastOnce()).checkUsingRateBySubscribers(rate);
    }

    @Test
    public void doUnusableRate() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);
        Rate returnedRate = new Rate(ID, "Last", 10d, 2L, true);
        when(rateRepository.doUnusableRateByRateId(rate)).thenReturn(returnedRate);

        Rate resultRate = rateService.doUnusableRate(rate);
        Assert.assertEquals(returnedRate, resultRate);

        verify(rateRepository, atLeastOnce()).doUnusableRateByRateId(rate);
    }
}