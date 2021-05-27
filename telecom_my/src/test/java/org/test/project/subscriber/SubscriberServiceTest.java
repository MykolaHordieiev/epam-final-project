package org.test.project.subscriber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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
        assertNotNull(resultSubscriber);
        assertEquals(resultSubscriber, new Subscriber());

        verify(subscriberRepository).insertSubscriber(subscriber);
    }

    @Test
    public void getSubscriberByIdRepositoryReturnSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber expectedSubscriber = new Subscriber(ID, "login", "password", 0, false);
        when(subscriberRepository.getById(subscriber)).thenReturn(Optional.of(expectedSubscriber));

        Subscriber resultSubscriber = subscriberService.getSubscriberById(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);

        verify(subscriberRepository).getById(subscriber);
    }

    @Test(expected = SubscriberException.class)
    public void getSubscriberByIdRepositoryReturnOptionalEmpty() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        when(subscriberRepository.getById(subscriber)).thenReturn(Optional.empty());

        subscriberService.getSubscriberById(subscriber);

        verify(subscriberRepository).getById(subscriber);
    }

    @Test
    public void getAll() {
        List<Subscriber> expected = Arrays.asList(new Subscriber(), new Subscriber());
        when(subscriberRepository.getAll()).thenReturn(expected);

        List<Subscriber> result = subscriberService.getAll();
        assertNotNull(result);
        assertEquals(expected, result);

        verify(subscriberRepository).getAll();
    }

    @Test
    public void lockSubscriberById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber returnedSubscriber = new Subscriber(ID, "login", "password", 0, false);
        Subscriber expectedSubscriber = new Subscriber(ID, "login", "password", 0, true);

        when(subscriberRepository.getById(subscriber)).thenReturn(Optional.of(returnedSubscriber));
        when(subscriberRepository.lockSubById(returnedSubscriber)).thenReturn(expectedSubscriber);

        Subscriber resultSubscriber = subscriberService.lockSubscriberById(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);

        verify(subscriberRepository).getById(subscriber);
        verify(subscriberRepository).lockSubById(returnedSubscriber);
    }

    @Test
    public void unlockSubscriberById() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber expectedSubscriber = new Subscriber(ID, "login", "password", 0, false);
        Subscriber returnedSubscriber = new Subscriber(ID, "login", "password", 0, true);
        when(subscriberRepository.getById(subscriber)).thenReturn(Optional.of(returnedSubscriber));
        when(subscriberRepository.unlockSubById(returnedSubscriber)).thenReturn(expectedSubscriber);

        Subscriber resultSubscriber = subscriberService.unlockSubscriberById(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);

        verify(subscriberRepository).getById(subscriber);
        verify(subscriberRepository).unlockSubById(returnedSubscriber);
    }

    @Test
    public void topUpBalanceWithoutIf() {
        double amount = 5;
        double balanceBefore = 10;
        double balanceNew = balanceBefore + amount;
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber subWithBalanceBefore = new Subscriber();
        subWithBalanceBefore.setBalance(balanceBefore);
        subWithBalanceBefore.setId(ID);
        Subscriber subWithNewBalance = new Subscriber();
        subWithNewBalance.setBalance(balanceNew);
        subWithNewBalance.setId(ID);

        when(subscriberRepository.getById(subscriber)).thenReturn(Optional.of(subWithBalanceBefore));
        when(subscriberRepository.topUpBalanceById(subWithBalanceBefore, balanceNew)).thenReturn(subWithNewBalance);

        Subscriber resultSubscriber = subscriberService.topUpBalance(subscriber, amount);
        assertNotNull(resultSubscriber);
        assertEquals(subWithNewBalance, resultSubscriber);

        verify(subscriberRepository).getById(subscriber);
        verify(subscriberRepository).topUpBalanceById(subWithBalanceBefore, balanceNew);
    }

    @Test
    public void topUpBalanceWithIf() {
        double amount = 20;
        double balanceBefore = -10;
        double balanceNew = balanceBefore + amount;
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        Subscriber subWithBalanceBefore = new Subscriber();
        subWithBalanceBefore.setBalance(balanceBefore);
        subWithBalanceBefore.setId(ID);
        Subscriber subWithNewBalance = new Subscriber();
        subWithNewBalance.setBalance(balanceNew);
        subWithNewBalance.setId(ID);
        Subscriber unlockSubscriber = new Subscriber();
        unlockSubscriber.setBalance(balanceNew);
        unlockSubscriber.setLock(false);

        when(subscriberRepository.getById(subscriber)).thenReturn(Optional.of(subWithBalanceBefore));
        when(subscriberRepository.topUpBalanceById(subWithBalanceBefore, balanceNew)).thenReturn(subWithNewBalance);
        when(subscriberRepository.unlockSubById(subWithNewBalance)).thenReturn(unlockSubscriber);

        Subscriber resultSubscriber = subscriberService.topUpBalance(subscriber, amount);
        assertNotNull(resultSubscriber);
        assertEquals(unlockSubscriber, resultSubscriber);

        verify(subscriberRepository).getById(subscriber);
        verify(subscriberRepository).topUpBalanceById(subWithBalanceBefore, balanceNew);
        verify(subscriberRepository).unlockSubById(subWithNewBalance);
    }

    @Test
    public void getSubscriberByLoginWhenRepositoryReturnSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin("login");
        Subscriber foundedSubscriber = new Subscriber();
        foundedSubscriber.setLogin("login");
        foundedSubscriber.setId(ID);
        Subscriber expectedSubscriber = new Subscriber(ID, "login", "password", 0, false);

        when(subscriberRepository.getByLogin(subscriber)).thenReturn(Optional.of(foundedSubscriber));
        when(subscriberRepository.getById(foundedSubscriber)).thenReturn(Optional.of(expectedSubscriber));

        Subscriber resultSubscriber = subscriberService.getSubscriberByLogin(subscriber);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);

        verify(subscriberRepository).getByLogin(subscriber);
        verify(subscriberRepository).getById(foundedSubscriber);
    }

    @Test(expected = SubscriberException.class)
    public void getSubscriberByLoginWhenRepositoryNotFoundSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin("login");

        when(subscriberRepository.getByLogin(subscriber)).thenReturn(Optional.empty());

        subscriberService.getSubscriberByLogin(subscriber);

        verify(subscriberRepository).getByLogin(subscriber);
    }
}