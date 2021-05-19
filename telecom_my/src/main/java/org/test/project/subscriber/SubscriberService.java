package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import org.test.project.User.UserLoginException;
import org.test.project.product.Product;
import org.test.project.product.ProductService;
import org.test.project.rate.Rate;
import org.test.project.rate.RateService;
import org.test.project.subscribing.Subscribing;
import org.test.project.subscribing.SubscribingService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final ProductService productService;
    private final RateService rateService;

    public Subscriber create(Subscriber subscriber) {
        return subscriberRepository.insertSubscriber(subscriber);
    }

    public Subscriber getSubscriberById(Long id) {
        return subscriberRepository.getById(id).orElseThrow(() -> new SubscriberException("subscriber with id: "
                + id + " doesn't exist"));
    }

    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    public Subscriber lockSubscriberById(Long id) {
        Subscriber subscriber = getSubscriberById(id);
        Subscriber returnedSubs = subscriberRepository.lockSubById(subscriber);
        returnedSubs.setLock(true);
        return returnedSubs;
    }

    public Subscriber unLockSubscriberById(Long id) {
        Subscriber subscriber = getSubscriberById(id);
        Subscriber returnedSubs = subscriberRepository.unLockSubById(subscriber);
        returnedSubs.setLock(false);
        return returnedSubs;
    }

    public Subscriber topUpBalance(Long id, Double amount) {
        Subscriber subscriber = getSubscriberById(id);
        double balanceBefore = subscriber.getBalance();
        double newBalance = subscriber.getBalance() + amount;
        Subscriber returnedSubscriber = subscriberRepository.topUpBalanceById(subscriber, newBalance);
        returnedSubscriber.setBalance(newBalance);
        if (balanceBefore < 0 && newBalance > 0) {
            unLockSubscriberById(returnedSubscriber.getId());
            returnedSubscriber.setLock(false);
        }
        return returnedSubscriber;
    }
}
