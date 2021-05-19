package org.test.project.User;

import lombok.RequiredArgsConstructor;
import org.test.project.infra.web.ModelAndView;
import org.test.project.subscriber.SubscriberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SubscriberService subscriberService;

    public ModelAndView login(HttpServletRequest request, HttpServletResponse respons) {
        String login = validEntryParameter(request.getParameter("login"), "login");
        String password = validEntryParameter(request.getParameter("password"), "password");
        User user = userService.loginUser(login, password);
        ModelAndView modelAndView;
        if (user.getUserRole().equals(UserRole.OPERATOR)) {
            modelAndView = ModelAndView.withView("/operator/home.jsp");
        } else {
            System.out.println(user.getId());
            user = subscriberService.getSubscriberById(user.getId());
            modelAndView = ModelAndView.withView("/subscriber/home.jsp");
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
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
