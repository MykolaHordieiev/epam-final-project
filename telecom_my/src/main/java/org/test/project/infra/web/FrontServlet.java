package org.test.project.infra.web;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.test.project.subscriber.SubscriberException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class FrontServlet extends HttpServlet {

    private final List<Controller> controllers;
    private final ExceptionHandler exceptionHandler;
    @Getter
    private final String name;
    @Getter
    private final String path;


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String controllerPath = getPath(req);
        String method = req.getMethod();
        ModelAndView modelAndView;
        try {
            modelAndView =  controllers.stream()
                    .map(controller -> controller.getRequestMatcher())
                    .flatMap(requestMatchers -> requestMatchers.stream())
                    .filter(requestMatcher -> requestMatcher.matcherPath(controllerPath))
                    .filter(requestMatcher -> requestMatcher.matcherMethod(method))
                    .findFirst()
                    .map(requestMatcher -> requestMatcher.getViewBiFunction())
                    .map(view -> view.apply(req, resp))
                    .orElseThrow(() -> new SubscriberException("page not found"));

        } catch (Exception ex) {
            modelAndView = exceptionHandler.handle(ex);
        }
        processModel(req, resp, modelAndView);
    }

    private void processModel(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws IOException, ServletException {
        if (modelAndView.isRedirect()) {
            resp.sendRedirect(req.getContextPath() + modelAndView.getView());
            return;
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(modelAndView.getView());
        fillAttributes(req, modelAndView);
        dispatcher.forward(req, resp);
    }

    private String getPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.substring(request.getContextPath().length() + path.length());
    }

    private void fillAttributes(HttpServletRequest request, ModelAndView modelAndView) {
        modelAndView.getAttributes().forEach(request::setAttribute);
    }
}
