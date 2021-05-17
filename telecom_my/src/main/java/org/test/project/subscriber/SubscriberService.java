package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import org.test.project.entity.Rate;

import java.util.List;

@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;


    public Subscriber create(Subscriber subscriber) {
        return subscriberRepository.insertSubscriber(subscriber);
    }

    public Subscriber getById(Long id) {
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
        Subscriber subscriber = getById(id);
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

    public Subscriber addSubscribing(Long idOfSubscriber, Long idOfProduct, Long idOfRate) {
        Subscriber subscriber = getById(idOfSubscriber);
        Rate rate = getRateById(idOfRate);
        if (checkStateOfSubscriber(subscriber, rate)){
            throw new FiledTransactionException("not enough funds to add subscribing");
        }
            return null;
    }

    private boolean checkStateOfSubscriber(Subscriber subscriber, Rate rate) {
        Double balance = subscriber.getBalance();
        Double cost = rate.getPrice();
        Double state = balance - cost;
        return state < 0;
    }
}
