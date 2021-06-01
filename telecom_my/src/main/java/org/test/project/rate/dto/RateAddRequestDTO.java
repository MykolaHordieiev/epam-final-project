package org.test.project.rate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateAddRequestDTO {

    private Long rateId;
    private String rateName;
    private Double price;
    private Long productId;
}
