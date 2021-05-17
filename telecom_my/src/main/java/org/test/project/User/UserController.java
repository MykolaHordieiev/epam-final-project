package org.test.project.User;

import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public ModelAndView login(HttpServletRequest request, HttpServletResponse respons) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = userService.loginUser(login, password);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        ModelAndView modelAndView;
        if (user.getUserRole().equals(UserRole.OPERATOR)) {
            modelAndView = ModelAndView.withView("/homePage/forOperator.jsp");
        } else {
            userService.checkLockSubscriber(user.getId());
            modelAndView = ModelAndView.withView("/homePage/forSubscriber.jsp");
        }
        modelAndView.setRedirect(true);
        return modelAndView;

    }
}
