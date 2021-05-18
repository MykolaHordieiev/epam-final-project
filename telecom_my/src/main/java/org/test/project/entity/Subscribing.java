package org.test.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.test.project.rate.Rate;
import org.test.project.subscriber.Subscriber;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscribing {

    private Subscriber subscriber;
    private Product product;
    private Rate rate;

}
