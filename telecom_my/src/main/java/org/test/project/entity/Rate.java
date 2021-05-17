package org.test.project.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Rate extends Entity {

    private Long id;
    private String name;
    private Double price;
    private Long product_id;
}
