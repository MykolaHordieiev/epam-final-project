package org.test.project.infra.web;

public class WebException extends RuntimeException implements MyException {

    WebException(String message) {
        super(message);
    }
}
