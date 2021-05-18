package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.product.Product;
import org.test.project.product.ProductService;
import org.test.project.rate.Rate;
import org.test.project.rate.RateService;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberException;
import org.test.project.subscriber.SubscriberService;

import java.util.List;

@RequiredArgsConstructor
public class SubscribingService {

    private final SubscribingRepository subscribingRepository;
    private final SubscriberService subscriberService;
    private final ProductService productService;
    private final RateService rateService;

    public Subscriber addSubscribing(Long idOfSubscriber, Long idOfProduct, Long idOfRate) {
        Subscriber subscriber = subscriberService.getSubscriberById(idOfSubscriber);
        Product product = productService.getProductById(idOfProduct);
        Rate rate = rateService.getRateById(idOfRate);
        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        if (!rate.getProductId().equals(product.getId())) {
            throw new SubscriberException("incorrect rate id: " + idOfRate + " for chose product");
        }
        Double balance = checkStateOfSubscriber(subscriber, rate);
        if (balance < 0) {
            subscriberService.lockSubscriberById(subscriber.getId());
            throw new SubscribingException("not enough money to add subscribing. " +
                    "You are locked, until you replenish your balance. " +
                    "Your balance = " + subscriber.getBalance());
        }
        subscriber.setBalance(balance);
        return subscribingRepository.addSubscribing(subscribing);
    }

    private Double checkStateOfSubscriber(Subscriber subscriber, Rate rate) {
        Double balance = subscriber.getBalance();
        Double cost = rate.getPrice();
        return balance - cost;
    }
}
