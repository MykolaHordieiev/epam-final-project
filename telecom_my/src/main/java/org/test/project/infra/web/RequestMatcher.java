package org.test.project.infra.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

@Data
@AllArgsConstructor
public class RequestMatcher {

    private String regexPath;
    private String regexMethod;
    private BiFunction<HttpServletRequest, HttpServletResponse, ?> biFunction;

//    public RequestMatcher(String regexPath, String regexMethod,
//                          BiFunction<HttpServletRequest, HttpServletResponse, ?> biFunction) {
//        this.regexPath = regexPath;
//        this.regexMethod = regexMethod;
//        this.biFunction = biFunction;
//    }


    public boolean matcherPath(String path) {
        return regexPath.equals(path);
    }

    public boolean matcherMethod(String method) {
        return regexMethod.equals(method);
    }
}
