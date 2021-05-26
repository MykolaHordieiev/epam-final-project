package org.test.project.subscribing;

import org.test.project.infra.web.MyException;

public class SubscribingException extends RuntimeException implements MyException {

    SubscribingException(String message) {
        super(message);
    }
}
