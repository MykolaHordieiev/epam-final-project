package org.test.project.subscriber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;
    @Mock
    private SubscriberMapper subscriberMapper;

    @InjectMocks
    private SubscriberService subscriberService;

    private static final Long ID = 1L;
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";

    @Test
    public void create() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(ID);
        subscriber.setLogin(LOGIN);
        subscriber.setPassword(PASSWORD);
        SubscriberCreateDTO subscriberCreateDTO = new SubscriberCreateDTO();
        subscriberCreateDTO.setLogin(LOGIN);
        subscriberCreateDTO.setPassword(PASSWORD);
        SubscriberCreateDTO returnSubscriberDTO = new SubscriberCreateDTO(ID, LOGIN, PASSWORD);

        when(subscriberRepository.insertSubscriber(subscriberCreateDTO)).thenReturn(returnSubscriberDTO);
        when(subscriberMapper.getSubscriberFromCreateDTO(returnSubscriberDTO)).thenReturn(subscriber);

        Subscriber resultSubscriber = subscriberService.create(subscriberCreateDTO);
        assertNotNull(resultSubscriber);
        assertEquals(subscriber, resultSubscriber);
    }

    @Test
    public void getSubscriberByIdRepositoryReturnSubscriber() {
        Subscriber expectedSubscriber = new Subscriber(ID, "login", "password", 0, false);

        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(expectedSubscriber));

        Subscriber resultSubscriber = subscriberService.getSubscriberById(ID);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);
    }

    @Test(expected = SubscriberException.class)
    public void getSubscriberByIdRepositoryReturnOptionalEmpty() {
        when(subscriberRepository.getById(ID)).thenReturn(Optional.empty());
        subscriberService.getSubscriberById(ID);
    }

    @Test
    public void getAll() {
        List<Subscriber> expected = Arrays.asList(new Subscriber(), new Subscriber());
        when(subscriberRepository.getAll(anyInt())).thenReturn(expected);

        List<Subscriber> result = subscriberService.getAll(anyInt());
        assertNotNull(result);
        assertEquals(expected, result);

        verify(subscriberRepository).getAll(anyInt());
    }

    @Test
    public void getCountOfHref() {
        double expected = 3.0;

        when(subscriberRepository.getCountOfRows()).thenReturn(11.0);

        double result = subscriberService.getCountOfHref();
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void lockSubscriberById() {
        Subscriber expectedSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 0, true);
        when(subscriberRepository.lockSubById(ID)).thenReturn(expectedSubscriber);
        Subscriber resultSubscriber = subscriberService.lockSubscriberById(ID);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);
    }

    @Test
    public void unlockSubscriberById() {
        Subscriber expectedSubscriber = new Subscriber(ID, "login", "password", 0, false);
        when(subscriberRepository.unlockSubById(ID)).thenReturn(expectedSubscriber);
        Subscriber resultSubscriber = subscriberService.unlockSubscriberById(ID);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);
    }

    @Test
    public void replenishBalanceWithoutIf() {
        double amount = 5;
        double balanceBefore = 10;
        double balanceNew = balanceBefore + amount;
        SubscriberReplenishDTO replenishDTO = new SubscriberReplenishDTO(ID, balanceNew);
        Subscriber subWithBalanceBefore = new Subscriber();
        subWithBalanceBefore.setBalance(balanceBefore);
        subWithBalanceBefore.setId(ID);
        Subscriber subWithNewBalance = new Subscriber();
        subWithNewBalance.setBalance(balanceNew);
        subWithNewBalance.setId(ID);

        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subWithBalanceBefore));
        when(subscriberRepository.replenishBalanceById(replenishDTO)).thenReturn(replenishDTO);
        when(subscriberMapper.getSubscriberFromReplenishDTO(replenishDTO)).thenReturn(subWithNewBalance);

        Subscriber resultSubscriber = subscriberService.replenishBalance(replenishDTO, amount);
        assertNotNull(resultSubscriber);
        assertEquals(subWithNewBalance, resultSubscriber);
    }

    @Test
    public void replenishBalanceWithIf() {
        double amount = 20;
        double balanceBefore = -10;
        double balanceNew = balanceBefore + amount;
        SubscriberReplenishDTO replenishDTO = new SubscriberReplenishDTO(ID, balanceNew);
        Subscriber subWithBalanceBefore = new Subscriber();
        subWithBalanceBefore.setBalance(balanceBefore);
        subWithBalanceBefore.setId(ID);
        Subscriber subWithNewBalance = new Subscriber();
        subWithNewBalance.setBalance(balanceNew);
        subWithNewBalance.setId(ID);
        Subscriber unlockSubscriber = new Subscriber();
        unlockSubscriber.setBalance(balanceNew);
        unlockSubscriber.setLock(false);

        when(subscriberRepository.getById(ID)).thenReturn(Optional.of(subWithBalanceBefore));
        when(subscriberRepository.replenishBalanceById(replenishDTO)).thenReturn(replenishDTO);
        when(subscriberMapper.getSubscriberFromReplenishDTO(replenishDTO)).thenReturn(subWithNewBalance);
        when(subscriberRepository.unlockSubById(ID)).thenReturn(unlockSubscriber);

        Subscriber resultSubscriber = subscriberService.replenishBalance(replenishDTO, amount);
        assertNotNull(resultSubscriber);
        assertEquals(subWithNewBalance, resultSubscriber);
    }

    @Test
    public void getSubscriberByLoginWhenRepositoryReturnSubscriber() {
        Subscriber expectedSubscriber = new Subscriber(ID, LOGIN, PASSWORD, 0, false);

        when(subscriberRepository.getByLogin(LOGIN)).thenReturn(Optional.of(expectedSubscriber));

        Subscriber resultSubscriber = subscriberService.getSubscriberByLogin(LOGIN);
        assertNotNull(resultSubscriber);
        assertEquals(expectedSubscriber, resultSubscriber);
    }

    @Test(expected = SubscriberException.class)
    public void getSubscriberByLoginWhenRepositoryNotFoundSubscriber() {
        when(subscriberRepository.getByLogin(LOGIN)).thenReturn(Optional.empty());
        subscriberService.getSubscriberByLogin(LOGIN);
    }
}