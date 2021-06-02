package org.test.project.rate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.rate.dto.RateAddRequestDTO;
import org.test.project.rate.dto.RateChangeRequestDTO;
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
    }

    @Test
    public void getRatesBySubscriberId() {
        List<Rate> expectedRateList = Arrays.asList(new Rate(), new Rate());

        when(rateRepository.getRatesBySubscriberId(ID)).thenReturn(expectedRateList);

        List<Rate> resultRateList = rateService.getRatesBySubscriberId(ID);
        Assert.assertEquals(expectedRateList, resultRateList);
    }

    @Test
    public void getRateByIdWhenRepositoryReturnRate() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);

        when(rateRepository.getRateById(ID)).thenReturn(Optional.of(rate));

        Rate resultRate = rateService.getRateById(ID);
        Assert.assertEquals(rate, resultRate);
    }

    @Test(expected = RateException.class)
    public void getRateByIdWhenRepositoryReturnEmptyOptional() {
        when(rateRepository.getRateById(ID)).thenReturn(Optional.empty());
        rateService.getRateById(ID);
    }

    @Test
    public void changeRateById() {
        RateChangeRequestDTO rateDTO = new RateChangeRequestDTO(ID, "super", 10d);

        when(rateRepository.changeRateById(rateDTO)).thenReturn(rateDTO);

        RateChangeRequestDTO resultRateDTO = rateService.changeRateById(rateDTO);
        Assert.assertEquals(rateDTO, resultRateDTO);
    }

    @Test
    public void addRateForProductWhenRepositoryReturnRate() {
        RateAddRequestDTO rateDTO = new RateAddRequestDTO();
        rateDTO.setRateName("100+chanel");
        rateDTO.setPrice(15d);
        RateAddRequestDTO expectedRateDTO = new RateAddRequestDTO(ID, "100+chanel", 15d, ID);

        when(rateRepository.addRateByProductId(rateDTO)).thenReturn(expectedRateDTO);

        RateAddRequestDTO resultRateDTO = rateService.addRateForProduct(rateDTO);
        Assert.assertEquals(expectedRateDTO, resultRateDTO);
    }

    @Test
    public void deleteRateById() {
        when(rateRepository.deleteRateById(ID)).thenReturn(ID);

        Long result = rateService.deleteRateById(ID);
        Assert.assertEquals(ID, result);
    }

    @Test
    public void checkUsingRateBeforeDelete() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);
        List<Subscriber> expectedSubscriberList = Arrays.asList(new Subscriber(), new Subscriber());

        when(rateRepository.checkUsingRateBySubscribers(ID)).thenReturn(expectedSubscriberList);

        List<Subscriber> resultSubscriberList = rateService.checkUsingRateBeforeDelete(ID);
        Assert.assertEquals(expectedSubscriberList, resultSubscriberList);
    }

    @Test
    public void doUnusableRate() {
        Rate rate = new Rate(ID, "Last", 10d, 2L, false);
        Rate returnedRate = new Rate(ID, "Last", 10d, 2L, true);

        when(rateRepository.doUnusableRateByRateId(rate)).thenReturn(returnedRate);

        Rate resultRate = rateService.doUnusableRate(rate);
        Assert.assertEquals(returnedRate, resultRate);
    }
}