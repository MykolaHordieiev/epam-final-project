package org.test.project.infra.web;

public interface ExceptionHandler {
    ModelAndView handle(Exception exception);
}
