package org.test.project.infra.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelAndView {
    private String view;
    private Map<String, Object> attributes = new HashMap<>();
    private boolean isRedirect;

    public static ModelAndView withView(String view){
        return ModelAndView.builder()
                .attributes(new HashMap<>())
                .isRedirect(false)
                .view(view)
                .build();
    }

    public void addAttribute(String name, Object attribute) {
        attributes.put(name, attribute);
    }

}
