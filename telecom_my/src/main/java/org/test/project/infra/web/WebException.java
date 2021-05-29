package org.test.project.infra.web;

public class WebException extends RuntimeException {

    public WebException(String message) {
        super(message);
    }
}
