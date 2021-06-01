package org.test.project.user;

import org.test.project.infra.web.Controller;
import org.test.project.infra.web.ModelAndView;
import org.test.project.infra.web.QueryValueResolver;
import org.test.project.infra.web.RequestMatcher;
import org.test.project.user.dto.UserLoginDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserController implements Controller {

    private final UserService userService;
    private final UserValidator validator;
    private final Map<UserRole, String> viewMap;
    private final QueryValueResolver queryValueResolver;
    private List<RequestMatcher> requestMatchers;

    public UserController(UserService userService, Map<UserRole, String> viewMap, UserValidator validator, QueryValueResolver queryValueResolver) {
        this.userService = userService;
        this.validator = validator;
        this.viewMap = viewMap;
        this.queryValueResolver = queryValueResolver;
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
        UserLoginDTO userLoginDTO = queryValueResolver.getObject(request, UserLoginDTO.class);
        validator.checkUser(userLoginDTO);
        User returnedUser = userService.loginUser(userLoginDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(viewMap.get(returnedUser.getUserRole()));
        HttpSession session = request.getSession();
        session.setAttribute("user", returnedUser);
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
