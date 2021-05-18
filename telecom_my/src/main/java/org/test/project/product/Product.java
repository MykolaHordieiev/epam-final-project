package org.test.project.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Product {

    private Long id;
    private String name;

    @Override
    public String toString(){
        return name;
    }
}
