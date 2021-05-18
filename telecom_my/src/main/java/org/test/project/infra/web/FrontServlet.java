package org.test.project.infra.web;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.test.project.User.UserController;
import org.test.project.operator.OperatorController;
import org.test.project.product.ProductController;
import org.test.project.rate.RateController;
import org.test.project.subscriber.SubscriberController;
import org.test.project.subscribing.SubscribingController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class FrontServlet extends HttpServlet {

    private final SubscriberController subscriberController;
    private final UserController userController;
    private final OperatorController operatorController;
    private final RateController rateController;
    private final ProductController productController;
    private final SubscribingController subscribingController;
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
            if (controllerPath.equals("/login") && method.equals("POST")) {
                modelAndView = userController.login(req, resp);
            } else if (controllerPath.equals("/subscriber") && method.equals("GET")) {
                modelAndView = subscriberController.getSubscriberById(req, resp);
            } else if (controllerPath.equals("/subscriber") && method.equals("POST")) {
                modelAndView = subscriberController.createSubscriber(req, resp);
            } else if (controllerPath.equals("/subscriber/all") && method.equals("GET")) {
                modelAndView = subscriberController.getAll(req, resp);
            } else if (controllerPath.equals("/subscriber/lock") && method.equals("POST")) {
                modelAndView = subscriberController.lockUser(req, resp);
            } else if (controllerPath.equals("/subscriber/unlock") && method.equals("POST")) {
                modelAndView = subscriberController.unLockUser(req, resp);
            }else if (controllerPath.equals("/subscriber/balance") && method.equals("POST")) {
                modelAndView = subscriberController.topUpTheBalance(req, resp);
            } else if (controllerPath.equals("/get/all/product") && method.equals("GET")) {
                modelAndView = productController.getAllProductsWithRate(req, resp);
            }   else if (controllerPath.equals("/add/subscribing") && method.equals("POST")) {
                modelAndView = subscribingController.addSubscribing(req, resp);
            } else if (controllerPath.equals("/rate/product") && method.equals("GET")) {
                modelAndView = rateController.getAllRates(req, resp);
            } else if (controllerPath.equals("/rate/info") && method.equals("GET")) {
                modelAndView = rateController.getRateById(req, resp);
            } else if (controllerPath.equals("/rate") && method.equals("POST")) {
                modelAndView = rateController.changeRates(req, resp);
            } else if (controllerPath.equals("/rate/add") && method.equals("GET")) {
                modelAndView = rateController.returnViewAddRates(req, resp);
            }else if (controllerPath.equals("/rate/add") && method.equals("POST")) {
                modelAndView = rateController.addRate(req, resp);
            } else  {
                modelAndView = ModelAndView.withView("/error/pagenotfound.jsp");
            }
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
