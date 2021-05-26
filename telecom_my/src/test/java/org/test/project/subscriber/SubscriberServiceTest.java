package org.test.project.subscriber;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private SubscriberService subscriberService;

    private static final Long ID = 1L;

    @Test
    public void create() {
        Subscriber subscriber = new Subscriber();
        when(subscriberRepository.insertSubscriber(subscriber)).thenReturn(subscriber);
        Subscriber resultSubscriber = subscriberService.create(subscriber);
        Assert.assertEquals(resultSubscriber, new Subscriber());
        verify(subscriberRepository).insertSubscriber(subscriber);
    }

    @Test
    public void getSubscriberByIdRepositoryReturnSubscriber() {
        Subscriber subscriber = new Subscriber();
        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subscriber));
        Subscriber resultSubscriber = subscriberService.getSubscriberById(ID);
        Assert.assertEquals(subscriber, resultSubscriber);
        verify(subscriberRepository).getById(ID);
    }
    @Test(expected = SubscriberException.class)
    public void getSubscriberByIdRepositoryReturnOptionalEmpty() {
        when(subscriberRepository.getById(ID)).thenReturn(Optional.empty());
        subscriberService.getSubscriberById(ID);
        verify(subscriberRepository).getById(ID);
    }

    @Test
    public void getAll() {
        List<Subscriber> expected = Arrays.asList(new Subscriber(), new Subscriber());
        when(subscriberRepository.getAll()).thenReturn(expected);
        List<Subscriber> result = subscriberService.getAll();
        Assert.assertEquals(expected, result);
        verify(subscriberRepository).getAll();
    }

    @Test
    public void lockSubscriberById() {
        Subscriber subscriber = new Subscriber();
        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subscriber));
        when(subscriberRepository.lockSubById(subscriber)).thenReturn(subscriber);
        Subscriber resultSubscriber = subscriberService.lockSubscriberById(ID);
        Assert.assertEquals(subscriber, resultSubscriber);
        verify(subscriberRepository, atLeastOnce()).getById(ID);
        verify(subscriberRepository, atLeastOnce()).lockSubById(subscriber);
    }

    @Test
    public void unlockSubscriberById() {
        Subscriber subscriber = new Subscriber();
        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subscriber));
        when(subscriberRepository.unlockSubById(subscriber)).thenReturn(subscriber);
        Subscriber resultSubscriber = subscriberService.unlockSubscriberById(ID);
        Assert.assertEquals(subscriber, resultSubscriber);
        verify(subscriberRepository, atLeastOnce()).getById(ID);
        verify(subscriberRepository, atLeastOnce()).unlockSubById(subscriber);
    }

    @Test
    public void topUpBalanceWithoutIf() {
        double amount = 5;
        double balanceBefore = 10;
        double balanceNew = balanceBefore + amount;
        Subscriber subWithBalanceBefore = new Subscriber();
        subWithBalanceBefore.setBalance(balanceBefore);
        Subscriber subWithNewBalance = new Subscriber();
        subWithNewBalance.setBalance(balanceNew);
        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subWithBalanceBefore));
        when(subscriberRepository.topUpBalanceById(subWithBalanceBefore, balanceNew)).thenReturn(subWithNewBalance);

        Subscriber resultSubscriber = subscriberService.topUpBalance(ID, amount);

        Assert.assertEquals(subWithNewBalance, resultSubscriber);
        verify(subscriberRepository, atLeastOnce()).getById(ID);
        verify(subscriberRepository, atLeastOnce()).topUpBalanceById(subWithBalanceBefore, balanceNew);
    }

    @Test
    public void topUpBalanceWithIf() {
        double amount = 20;
        double balanceBefore = -10;
        double balanceNew = balanceBefore + amount;
        Subscriber subWithBalanceBefore = new Subscriber();
        subWithBalanceBefore.setBalance(balanceBefore);
        Subscriber subWithNewBalance = new Subscriber();
        subWithNewBalance.setBalance(balanceNew);
        Subscriber unlockSubscriber = new Subscriber();
        unlockSubscriber.setBalance(balanceNew);
        unlockSubscriber.setLock(false);
        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subWithBalanceBefore));
        when(subscriberRepository.topUpBalanceById(subWithBalanceBefore, balanceNew)).thenReturn(subWithNewBalance);
        when(subscriberRepository.unlockSubById(subWithNewBalance)).thenReturn(unlockSubscriber);

        Subscriber resultSubscriber = subscriberService.topUpBalance(ID, amount);

        Assert.assertEquals(unlockSubscriber, resultSubscriber);
        verify(subscriberRepository, atLeastOnce()).getById(ID);
        verify(subscriberRepository, atLeastOnce()).topUpBalanceById(subWithBalanceBefore, balanceNew);
        verify(subscriberRepository, atLeastOnce()).unlockSubById(resultSubscriber);
    }
}