package org.test.project.User;

import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class UserController implements Controller {

    private final UserService userService;
    private final SubscriberService subscriberService;
    private List<RequestMatcher> requestMatchers;

    public UserController(UserService userService, SubscriberService subscriberService) {
        this.userService = userService;
        this.subscriberService = subscriberService;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/login", "POST", this::login));
        requestMatchers.add(new RequestMatcher("/logout", "GET", this::logout));
        return requestMatchers;
    }

    public ModelAndView login(HttpServletRequest request, HttpServletResponse respons) {
        String login = validEntryParameter(request.getParameter("login"), "login");
        String password = validEntryParameter(request.getParameter("password"), "password");
        User user = userService.loginUser(login, password);
        ModelAndView modelAndView;
        if (user.getUserRole().equals(UserRole.OPERATOR)) {
            modelAndView = ModelAndView.withView("/operator/home.jsp");
        } else {
            user = subscriberService.getSubscriberById(user.getId());
            modelAndView = ModelAndView.withView("/subscriber/home.jsp");
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/index.jsp");
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    private String validEntryParameter(String value, String parameter) {
        if (value.equals("")) {
            throw new UserLoginException("entry value cannot be empty: " + parameter);
        }
        return value;
    }
}
