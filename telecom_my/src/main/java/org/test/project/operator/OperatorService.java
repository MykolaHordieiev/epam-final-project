package org.test.project.operator;

import lombok.RequiredArgsConstructor;
import org.test.project.entity.Product;
import org.test.project.entity.Rate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OperatorService {

    private final OperatorRepository operatorRepository;

    public void addRateForProduct(String nameOfRate, double price, Long idOfProduct) {
        operatorRepository.addRateByProductId(nameOfRate, price, idOfProduct);
    }

    public boolean deleteRateForProduct(Long id) {
        return operatorRepository.deleteRateByProductId(id);
    }

    public List<Product> getAllProduct() {
        return operatorRepository.getAllProducts();
    }

    public List<Rate> getAllRates() {
        return operatorRepository.getAllRatesOfProduct();
    }

    public void doChangeRateById(Long id,String name,Double price){
        if(name.equals("")&&price.equals(0.0)){
            throw new FiledChangeRate("please," +
                    " fill the corresponding line (lines) with the value and try again do change");
        }
        operatorRepository.changeRate(id, name, price);
    }
}


