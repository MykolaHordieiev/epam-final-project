package org.test.project.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rate {

    private Long id;
    private String name;
    private Double price;
    private Long productId;

    @Override
    public String toString(){
        return name;
    }
}
