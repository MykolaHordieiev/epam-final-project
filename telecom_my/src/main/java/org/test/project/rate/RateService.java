package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import org.test.project.subscriber.Subscriber;

import java.util.List;

@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;

    public List<Rate> getRatesByProductId(Long productId) {
        return rateRepository.getRatesByProduct(productId);
    }

    public Rate getRateById(long id) {
        return rateRepository.getRateById(id).orElseThrow(() -> new RateException("rate by id: " +
                id + " not found"));
    }

    public Rate changeRateById(Rate rate) {
        rate.setProductId(getRateById(rate.getId()).getProductId());
        return rateRepository.changeRateById(rate);
    }

    public Rate addRateForProduct(Rate rate) {
        return rateRepository.addRateByProductId(rate).orElseThrow(() -> new RateException("filed create new rate"));
    }

    public Rate deleteRateById(Rate rate) {
        rate.setProductId(getRateById(rate.getId()).getProductId());
        return rateRepository.deleteRateById(rate);
    }

    public List<Subscriber> checkUsingRateBeforeDelete(Rate rate) {
        return rateRepository.checkUsingRateBySubscribers(rate);
    }

    public Rate doUnusableRate(Rate rate) {
        return rateRepository.doUnusableRateByRateId(rate);
    }
}
