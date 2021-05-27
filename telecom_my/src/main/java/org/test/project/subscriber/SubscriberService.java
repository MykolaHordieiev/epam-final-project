package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public Subscriber create(Subscriber subscriber) {
        return subscriberRepository.insertSubscriber(subscriber);
    }

    public Subscriber getSubscriberById(Subscriber subscriber) {
        return subscriberRepository.getById(subscriber).orElseThrow(() -> new SubscriberException("subscriber with id: "
                + subscriber.getId() + " doesn't exist"));
    }

    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    public Subscriber lockSubscriberById(Subscriber subscriber) {
        Subscriber returnedSubscriber = getSubscriberById(subscriber);
        return subscriberRepository.lockSubById(returnedSubscriber);
    }

    public Subscriber unlockSubscriberById(Subscriber subscriber) {
        Subscriber returnedSubscriber = getSubscriberById(subscriber);
        return subscriberRepository.unlockSubById(returnedSubscriber);
    }

    public Subscriber topUpBalance(Subscriber subscriber, Double amount) {
        Subscriber subscriberBeforeReplenish = getSubscriberById(subscriber);
        double balanceBefore = subscriberBeforeReplenish.getBalance();
        double newBalance = subscriberBeforeReplenish.getBalance() + amount;
        Subscriber subscriberAfterReplenish = subscriberRepository.topUpBalanceById(subscriberBeforeReplenish, newBalance);
        if (balanceBefore < 0 && newBalance > 0) {
            subscriberAfterReplenish = subscriberRepository.unlockSubById(subscriberAfterReplenish);
        }
        return subscriberAfterReplenish;
    }

    public Subscriber getSubscriberByLogin(Subscriber subscriber) {
        Subscriber foundSubscriber = subscriberRepository.getByLogin(subscriber).orElseThrow(
                () -> new SubscriberException("Subscriber with login " + subscriber.getLogin() + " not found"));
        return getSubscriberById(foundSubscriber);
    }
}
