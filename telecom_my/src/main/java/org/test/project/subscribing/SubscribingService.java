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
        double balance = checkStateOfSubscriber(subscriber, rate);
        subscribing.getSubscriber().setBalance(balance);
        return subscribingRepository.addSubscribing(subscribing);
    }

    public List<Subscribing> getSubscribing(Long id) {
        List<Subscribing> list = subscribingRepository.getSubscribingBySubscriberId(id);
        if (!list.isEmpty()) {
            for (Subscribing subs : list) {
                Long idOfProduct = subs.getProduct().getId();
                System.out.println(idOfProduct);
                Long idOfRate = subs.getRate().getId();
                subs.getProduct().setName(productService.getProductById(idOfProduct).getName());
                subs.getRate().setName(rateService.getRateById(idOfRate).getName());
            }
        }
        return list;
    }

    private Double checkStateOfSubscriber(Subscriber subscriber, Rate rate) {
        Double balance = subscriber.getBalance();
        Double cost = rate.getPrice();
        return balance - cost;
    }
}
