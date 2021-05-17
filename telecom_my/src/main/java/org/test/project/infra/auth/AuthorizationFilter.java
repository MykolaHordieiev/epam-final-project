package org.test.project.infra.auth;


import org.test.project.User.User;
import org.test.project.User.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String pathWithoutContext = getPathWithoutContext(request);
        boolean hasAccess = false;
        if (pathWithoutContext.equals("/home.jsp")) {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            hasAccess = user.getUserRole().equals(UserRole.SUBSCRIBER) || user.getUserRole().equals(UserRole.OPERATOR);
        }
        if (hasAccess) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("error/forbiden.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void destroy() {

    }

    private String getPathWithoutContext(HttpServletRequest httpServletRequest) {
        int contextPathLength = httpServletRequest.getContextPath().length();
        return httpServletRequest.getRequestURI().substring(contextPathLength);
    }
}
