package org.test.project.product;


import org.test.project.infra.exception.TelecomException;

public class ProductException extends TelecomException {

    ProductException(String message) {
        super(message);
    }
}
