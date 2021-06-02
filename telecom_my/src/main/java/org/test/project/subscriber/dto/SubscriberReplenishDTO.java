package org.test.project.subscriber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberReplenishDTO {

    private Long id;
    private Double balance;
}
