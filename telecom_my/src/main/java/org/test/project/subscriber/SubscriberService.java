package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

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
        return subscriberRepository.lockSubById(subscriber);
    }

    public Subscriber unlockSubscriberById(Long id) {
        Subscriber subscriber = getSubscriberById(id);
        return subscriberRepository.unlockSubById(subscriber);
    }

    public Subscriber topUpBalance(Long id, Double amount) {
        Subscriber subscriber = getSubscriberById(id);
        double balanceBefore = subscriber.getBalance();
        double newBalance = subscriber.getBalance() + amount;
        Subscriber returnedSubscriber = subscriberRepository.topUpBalanceById(subscriber, newBalance);
        if (balanceBefore < 0 && newBalance > 0) {
            returnedSubscriber = subscriberRepository.unlockSubById(returnedSubscriber);
        }
        return returnedSubscriber;
    }
}
