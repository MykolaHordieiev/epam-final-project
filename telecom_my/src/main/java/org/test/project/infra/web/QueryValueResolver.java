package org.test.project.infra.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class QueryValueResolver {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public <T> T getObject(HttpServletRequest req, Class<T> tClass) {
        Map<String, String> valuesMap = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = req.getParameter(name);
            valuesMap.put(name, value);
        }
        return objectMapper.convertValue(valuesMap, tClass);
    }
}
