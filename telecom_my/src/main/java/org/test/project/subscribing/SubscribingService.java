package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.product.Product;
import org.test.project.product.ProductRepository;
import org.test.project.rate.Rate;
import org.test.project.rate.RateRepository;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberException;

import java.util.List;

@RequiredArgsConstructor
public class SubscribingService {

    private final SubscribingRepository subscribingRepository;

    public Subscriber addSubscribing(Subscriber subscriber, Product product, Rate rate) {
        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        double balance = getNewBalance(subscriber, rate);
        subscribing.getSubscriber().setBalance(balance);
        return subscribingRepository.addSubscribing(subscribing);
    }

    public List<Subscribing> getSubscribing(Subscriber subscriber) {
        List<Subscribing> subscribingList = subscribingRepository.getSubscribingBySubscriberId(subscriber);
        for (Subscribing subs : subscribingList) {
            subs.setProduct(subscribingRepository.getProduct(subs.getProduct()));
            subs.setRate(subscribingRepository.getRateBy(subs.getRate()));
        }
        return subscribingList;
    }

    public Double getNewBalance(Subscriber subscriber, Rate rate) {
        Double balance = subscriber.getBalance();
        Double cost = rate.getPrice();
        return balance - cost;
    }
}
