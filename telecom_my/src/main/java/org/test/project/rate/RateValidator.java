package org.test.project.rate;

public class RateValidator {

    public Rate checkEmptyEntryParameter(String name, String newPrice) {
        Rate rate = new Rate();
        rate.setName(checkName(name));
        rate.setPrice(checkPrice(newPrice));
        return rate;
    }

    public String checkName(String name) {
        if (name.equals("")) {
            throw new RateException("Field with name cannot be empty");
        }
        return name;
    }

    public Double checkPrice(String price) {
        if (price.equals("")) {
            throw new RateException("Field with price cannot be empty");
        }
        Double newPrice = Double.parseDouble(price);
        if (newPrice < 0) {
            throw new RateException("price cannot be < 0");
        }
        return newPrice;
    }
}