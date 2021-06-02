package org.test.project.rate;

import lombok.RequiredArgsConstructor;

import org.test.project.rate.dto.RateAddRequestDTO;
import org.test.project.rate.dto.RateChangeRequestDTO;
import org.test.project.subscriber.Subscriber;

import java.util.List;

@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;

    public List<Rate> getRatesByProductId(Long productId) {
        return rateRepository.getRatesByProduct(productId);
    }

    public List<Rate> getRatesBySubscriberId(Long subscriberId) {
        return  rateRepository.getRatesBySubscriberId(subscriberId);
    }

    public Rate getRateById(Long id) {
        return rateRepository.getRateById(id).orElseThrow(() -> new RateException("rate by id: " +
                id + " not found"));
    }

    public RateChangeRequestDTO changeRateById(RateChangeRequestDTO rateDTO) {
        return rateRepository.changeRateById(rateDTO);
    }

    public RateAddRequestDTO addRateForProduct(RateAddRequestDTO rateDTO) {
        return rateRepository.addRateByProductId(rateDTO);
    }

    public Long deleteRateById(Long id) {
        return rateRepository.deleteRateById(id);
    }

    public List<Subscriber> checkUsingRateBeforeDelete(Long id) {
        return rateRepository.checkUsingRateBySubscribers(id);
    }

    public Rate doUnusableRate(Rate rate) {
        return rateRepository.doUnusableRateByRateId(rate);
    }

}
