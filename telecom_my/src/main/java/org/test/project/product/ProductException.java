package org.test.project.product;

import org.test.project.infra.web.MyException;

public class ProductException extends RuntimeException implements MyException {
    ProductException(String message) {
        super(message);
    }
}
