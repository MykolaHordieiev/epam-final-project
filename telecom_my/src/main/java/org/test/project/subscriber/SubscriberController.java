package org.test.project.subscriber;


import lombok.AllArgsConstructor;
import org.test.project.User.User;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@AllArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;


//        addMapping("GET", "/subscriber", this::getSubscriberById);
//        addMapping("POST","/checkExist",this::checkExistUserInSystem);
//        addMapping("POST", "/subscriber", this::createSubscriber);
//        addMapping("GET", "/subscriber/all", this::getAll);


    public ModelAndView getSubscriberById(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("id"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/infobyid.jsp");
        modelAndView.addAttribute("subscriber", subscriberService.getSubscriberById(id));
        modelAndView.addAttribute("subscriptions",subscriberService.getSubscribing(id));
        return modelAndView;
    }

    public ModelAndView createSubscriber(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin(login);
        subscriber.setPassword(password);
        Subscriber createdSubscriber = subscriberService.create(subscriber);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + createdSubscriber.getId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView getAll(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/subscriber/all.jsp");
        modelAndView.addAttribute("subscribers", subscriberService.getAll());
        return modelAndView;
    }

    public ModelAndView lockUser(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        System.out.println(id);
        subscriberService.lockSubscriberById(Long.parseLong(id));
        return getSubscriberById(request, response);
    }

    public ModelAndView unLockUser(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        System.out.println(id);
        subscriberService.unLockSubscriberById(Long.parseLong(id));
        return getSubscriberById(request, response);
    }

    public ModelAndView topUpTheBalance(HttpServletRequest request, HttpServletResponse response) {
        String amount = request.getParameter("amount");
        User subscriber = getUserOfSession(request);
        subscriberService.topUpBalance(subscriber.getId(), Double.parseDouble(amount));
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + subscriber.getId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView addSubscribing(HttpServletRequest request, HttpServletResponse response) {
        Long idOfProduct = Long.parseLong(request.getParameter("idOfProduct"));
        Long idofRate = Long.parseLong(request.getParameter("idOfRate"));
        Subscriber subscriber = getUserOfSession(request);
        subscriberService.addSubscribing(subscriber.getId(),idOfProduct,idofRate);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + subscriber.getId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private Subscriber getUserOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Subscriber) session.getAttribute("user");
    }
}