package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import org.test.project.entity.Product;
import org.test.project.rate.Rate;
import org.test.project.entity.Subscribing;

import java.util.List;

@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;


    public Subscriber create(Subscriber subscriber) {
        return subscriberRepository.insertSubscriber(subscriber);
    }

    public Subscriber getSubscriberById(Long id) {
        return subscriberRepository.getById(id).orElseThrow(() -> new FiledTransactionException("subscriber with id: "
                + id + " doesn't exist"));
    }

    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    public boolean lockSubscriberById(Long id) {
        return subscriberRepository.lockSubById(id);
    }

    public boolean unLockSubscriberById(Long id) {
        return subscriberRepository.unLockSubById(id);
    }

    public Subscriber topUpBalance(Long id, Double amount) {
        Subscriber subscriber = getSubscriberById(id);
        Double balnceOfSubscriber = subscriber.getBalance() + amount;
        if (!subscriberRepository.topUpBalanceById(id, balnceOfSubscriber)) {
            subscriber.setBalance(balnceOfSubscriber);
        } else {
            throw new FiledTransactionException("filed top Up Balance");
        }
        return subscriber;
    }

    public Rate getRateById(Long id) {
        return subscriberRepository.getRate(id).orElseThrow(() -> new FiledTransactionException("rate with id: "
                + id + " doesn't exist"));
    }

    public Product getProductById(Long id) {
        return subscriberRepository.getProduct(id).orElseThrow(() -> new FiledTransactionException("product with id: "
                + id + " doesn't exist"));
    }

    public Subscriber addSubscribing(Long idOfSubscriber, Long idOfProduct, Long idOfRate) {
        Subscriber subscriber = getSubscriberById(idOfSubscriber);
        Product product = getProductById(idOfProduct);
        Rate rate = getRateById(idOfRate);
        Subscribing subscribing = new Subscribing(subscriber, product, rate);
        if (!rate.getProductId().equals(product.getId())) {
            throw new SubscriberException("incorrect rate id: " + idOfRate + " for chose product");
        }
        Double balance = checkStateOfSubscriber(subscriber, rate);
        if (balance < 0) {
            throw new FiledTransactionException("not enough money to add subscribing");
        }
        subscriber.setBalance(balance);
        return subscriberRepository.addSubscribing(subscribing);
    }

    public List<Subscribing> getSubscribing(Long id) {
        List<Subscribing> list = subscriberRepository.getSubscribingBySubscriberId(id);
        if (!list.isEmpty()) {
            for (Subscribing subs : list) {
                Long idOfProduct = subs.getProduct().getId();
                Long idOfRate = subs.getRate().getId();
                subs.getProduct().setName(getProductById(idOfProduct).getName());
                subs.getRate().setName(getRateById(idOfRate).getName());
                System.out.println(list);
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
