package org.test.project.subscriber;

import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;

public class SubscriberMapper {

    public Subscriber getSubscriberFromCreateDTO(SubscriberCreateDTO dto) {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(dto.getId());
        subscriber.setLogin(dto.getLogin());
        return subscriber;
    }

    public Subscriber getSubscriberFromReplenishDTO(SubscriberReplenishDTO replenishDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(replenishDTO.getId());
        subscriber.setBalance(replenishDTO.getBalance());
        return subscriber;
    }
}
