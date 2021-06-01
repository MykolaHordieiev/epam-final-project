package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.rate.dto.RateAddSubscribingDTO;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;

import java.util.List;

@RequiredArgsConstructor
public class SubscribingService {

    private final SubscribingRepository subscribingRepository;

    public SubscriberAddSubscribingDTO addSubscribing(SubscriberAddSubscribingDTO subscriberDTO, Long productId, RateAddSubscribingDTO rateDTO) {
        double newBalance = getNewBalance(subscriberDTO, rateDTO);
        subscriberDTO.setBalance(newBalance);
        return subscribingRepository.addSubscribing(subscriberDTO, productId, rateDTO.getRateId());
    }

    public List<Subscribing> getSubscribing(Long id) {
        return subscribingRepository.getSubscribingBySubscriberId(id);
    }

    public Double getNewBalance(SubscriberAddSubscribingDTO subscriberDTO, RateAddSubscribingDTO rateDTO) {
        Double balance = subscriberDTO.getBalance();
        Double cost = rateDTO.getPrice();
        return balance - cost;
    }
}
