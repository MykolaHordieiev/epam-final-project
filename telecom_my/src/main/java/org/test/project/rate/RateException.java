package org.test.project.rate;

import org.test.project.infra.web.MyException;

public class RateException extends RuntimeException implements MyException {

    RateException(String message) {
        super(message);
    }
}
