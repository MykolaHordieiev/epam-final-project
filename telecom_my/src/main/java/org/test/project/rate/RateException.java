package org.test.project.rate;

import org.test.project.infra.exception.TelecomException;

public class RateException extends TelecomException {

    RateException(String message) {
        super(message);
    }
}
