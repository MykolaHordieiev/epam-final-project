package org.test.project.infra.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
public class FileDownloadServlet extends HttpServlet {

    private final List<Controller> controllers;
    private final ExceptionHandler exceptionHandler;
    @Getter
    private final String name;
    @Getter
    private final String path;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        String controllerPath = getPath(request);
        String method = request.getMethod();
        try {
            controllers.stream()
                    .map(controller -> controller.getRequestMatcher())
                    .flatMap(requestMatchers -> requestMatchers.stream())
                    .filter(requestMatcher -> requestMatcher.matcherPath(controllerPath))
                    .filter(requestMatcher -> requestMatcher.matcherMethod(method))
                    .findFirst()
                    .map(requestMatcher -> requestMatcher.getResponseBiConsumer())
                    .ifPresent(consumer -> consumer.accept(request, response));
        } catch (Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    private String getPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.substring(request.getContextPath().length() + path.length());
    }
}
