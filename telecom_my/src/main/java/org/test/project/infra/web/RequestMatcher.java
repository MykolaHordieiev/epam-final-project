package org.test.project.infra.web;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Data
public class RequestMatcher {

    private String regexPath;
    private String regexMethod;
    private BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> viewBiFunction;
    private BiConsumer<HttpServletRequest, HttpServletResponse> responseBiConsumer;

    public RequestMatcher(String regexPath, String regexMethod,
                          BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> viewBiFunction) {
        this.regexPath = regexPath;
        this.regexMethod = regexMethod;
        this.viewBiFunction = viewBiFunction;
    }

    public RequestMatcher(String regexPath, String regexMethod,
                          BiConsumer<HttpServletRequest, HttpServletResponse> responseBiConsumer) {
        this.regexPath = regexPath;
        this.regexMethod = regexMethod;
        this.responseBiConsumer = responseBiConsumer;
    }

    public boolean matcherPath(String path) {
        return regexPath.equals(path);
    }

    public boolean matcherMethod(String method) {
        return regexMethod.equals(method);
    }
}
