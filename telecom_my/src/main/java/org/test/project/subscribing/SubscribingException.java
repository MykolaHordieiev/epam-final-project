package org.test.project.subscribing;


import org.test.project.infra.exception.TelecomException;

public class SubscribingException extends TelecomException {

    SubscribingException(String message) {
        super(message);
    }
}
