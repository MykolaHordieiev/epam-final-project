package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.infra.web.ModelAndView;
import org.test.project.subscriber.Subscriber;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class SubscribingController {

    private final SubscribingService subscribingService;
    private final SubscriberService subscriberService;

    public ModelAndView addSubscribing(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.parseLong(request.getParameter("productId"));
        Long rateId = Long.parseLong(request.getParameter("rateId"));
        Subscriber subscriber = getUserOfSession(request);
        Subscriber returnedSubscriber = subscribingService.addSubscribing(subscriber.getId(), productId, rateId);
        double balanceAfterAddSubscribing = returnedSubscriber.getBalance();
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + returnedSubscriber.getId());
        if (balanceAfterAddSubscribing < 0) {
            subscriberService.lockSubscriberById(returnedSubscriber.getId());
            String message = "subscribing was add, but " +
                    "you are locked, until you replenish your balance. " +
                    "Your balance = " + balanceAfterAddSubscribing +
                    " Please, top up your balance on: " + Math.abs(balanceAfterAddSubscribing);
            modelAndView.setView("/subscriber/lock.jsp");
            modelAndView.addAttribute("message", message);
            HttpSession session = request.getSession();
            session.setAttribute("user",returnedSubscriber);
        }
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private Subscriber getUserOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Subscriber) session.getAttribute("user");
    }
}
