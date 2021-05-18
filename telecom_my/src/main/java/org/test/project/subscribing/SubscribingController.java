package org.test.project.subscribing;

import lombok.RequiredArgsConstructor;
import org.test.project.infra.web.ModelAndView;
import org.test.project.subscriber.Subscriber;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class SubscribingController {

    private  final SubscribingService subscribingService;

    public ModelAndView addSubscribing(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.parseLong(request.getParameter("productId"));
        Long rateId = Long.parseLong(request.getParameter("rateId"));
        Subscriber subscriber = getUserOfSession(request);
        subscribingService.addSubscribing(subscriber.getId(), productId, rateId);
        ModelAndView modelAndView = ModelAndView.withView("/service/subscriber?id=" + subscriber.getId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private Subscriber getUserOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Subscriber) session.getAttribute("user");
    }
}
