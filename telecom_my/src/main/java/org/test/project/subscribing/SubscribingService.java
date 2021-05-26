package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberException;

import java.util.List;

@RequiredArgsConstructor
public class SubscribingService {

    private final SubscribingRepository subscribingRepository;

    public Subscriber addSubscribing(Subscriber subscriber, Product product, Rate rate) {
        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        if (!rate.getProductId().equals(product.getId())) {
            throw new SubscribingException("incorrect rate id: " + rate.getId() + " for chose product");
        }
        double balance = getNewBalance(subscriber, rate);
        subscribing.getSubscriber().setBalance(balance);
        return subscribingRepository.addSubscribing(subscribing);
    }

    public List<Subscribing> getSubscribing(Long id) {
        return subscribingRepository.getSubscribingBySubscriberId(id);
    }

    private Double getNewBalance(Subscriber subscriber, Rate rate) {
        Double balance = subscriber.getBalance();
        Double cost = rate.getPrice();
        return balance - cost;
    }
}
