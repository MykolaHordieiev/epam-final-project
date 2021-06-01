package org.test.project.rate;


import org.test.project.rate.dto.RateAddRequestDTO;
import org.test.project.rate.dto.RateChangeRequestDTO;

public class RateValidator {

    public RateChangeRequestDTO checkEmptyEntryChangeParameter(RateChangeRequestDTO rateDTO) {
        checkName(rateDTO.getRateName());
        checkPrice(rateDTO.getPrice());
        return rateDTO;
    }

    public RateAddRequestDTO checkEmptyEntryAddParameter(RateAddRequestDTO rateDTO) {
        checkName(rateDTO.getRateName());
        checkPrice(rateDTO.getPrice());
        return rateDTO;
    }

    private String checkName(String name) {
        if (name.equals("")) {
            throw new RateException("Field with name cannot be empty");
        }
        return name;
    }

    private Double checkPrice(Double price) {
        if (price == null) {
            throw new RateException("Field with price cannot be empty");
        }
        if (price < 0) {
            throw new RateException("price cannot be < 0");
        }
        return price;
    }
}