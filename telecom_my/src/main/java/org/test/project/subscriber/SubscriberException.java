package org.test.project.subscriber;


import org.test.project.infra.exception.TelecomException;

public class SubscriberException extends TelecomException {

    SubscriberException(String message) {
        super(message);
    }
}
