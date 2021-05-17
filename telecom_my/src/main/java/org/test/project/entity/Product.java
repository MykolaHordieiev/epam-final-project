package org.test.project.entity;

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
