package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.rate.dto.RateAddSubscribingDTO;
import org.test.project.subscriber.dto.SubscriberAddSubscribingDTO;

import java.math.BigDecimal;
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

    private double getNewBalance(SubscriberAddSubscribingDTO subscriberDTO, RateAddSubscribingDTO rateDTO) {
        BigDecimal balance = BigDecimal.valueOf(subscriberDTO.getBalance());
        BigDecimal cost = BigDecimal.valueOf(rateDTO.getPrice());
        return Double.parseDouble(balance.subtract(cost).toString());
    }
}
