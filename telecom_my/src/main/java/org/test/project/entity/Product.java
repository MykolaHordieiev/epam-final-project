package org.test.project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Product extends Entity {

    private Long id;
    private String name;
}
