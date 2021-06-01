package org.test.project.infra.web;

import org.test.project.infra.exception.TelecomException;

public class WebException extends TelecomException {

    public WebException(String message) {
        super(message);
    }
}
