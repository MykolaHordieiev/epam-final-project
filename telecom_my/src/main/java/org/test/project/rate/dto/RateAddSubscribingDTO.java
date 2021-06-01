package org.test.project.rate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateAddSubscribingDTO {

    private Long rateId;
    private Double price;
}
