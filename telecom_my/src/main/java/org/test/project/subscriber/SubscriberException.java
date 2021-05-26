package org.test.project.subscriber;

import org.test.project.infra.web.MyException;

public class SubscriberException extends RuntimeException implements MyException {

    SubscriberException(String message) {
        super(message);
    }
}
