package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import org.test.project.entity.Rate;

import java.util.List;

@RequiredArgsConstructor
public class RateService {

   private final RateRepository rateRepository;

    public List<Rate> getRatesByProductId(Long productId) {
        return rateRepository.getRatesByProduct(productId);
    }

    public Rate getRateById(long id) {
        return rateRepository.getRateById(id).orElseThrow(()->new RateException("rate by id: "+
                id+" not found"));
    }
}
