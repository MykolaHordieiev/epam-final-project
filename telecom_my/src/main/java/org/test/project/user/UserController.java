package org.test.project.user;

import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.subscriber.SubscriberService;
import org.test.project.validator.ValidatorEntryParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserController implements Controller {

    private final UserService userService;
    private final SubscriberService subscriberService;
    private final ValidatorEntryParameter validator;
    private List<RequestMatcher> requestMatchers;

    public UserController(UserService userService, SubscriberService subscriberService,ValidatorEntryParameter validator) {
        this.userService = userService;
        this.subscriberService = subscriberService;
        this.validator = validator;
        requestMatchers = new ArrayList<>();
    }

    @Override
    public List<RequestMatcher> getRequestMatcher() {
        requestMatchers.add(new RequestMatcher("/login", "POST", this::login));
        requestMatchers.add(new RequestMatcher("/logout", "POST", this::logout));
        requestMatchers.add(new RequestMatcher("/change/locale", "POST", this::changeLocale));
        return requestMatchers;
    }

    public ModelAndView changeLocale(HttpServletRequest request, HttpServletResponse response) {
        String selectedLocale = request.getParameter("selectedLocale");
        String view = request.getParameter("view");
        Locale locale = new Locale(selectedLocale);
        HttpSession session = request.getSession(false);
        session.setAttribute("selectedLocale", locale);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(view);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView login(HttpServletRequest request, HttpServletResponse respons) {
        String login = validator.checkEmptyEntryParameter(request.getParameter("login"), "login");
        String password = validator.checkEmptyEntryParameter(request.getParameter("password"), "password");
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
}
