package org.test.project.infra.auth;


import org.test.project.User.User;
import org.test.project.User.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationFilter implements Filter {

    private List<AuthorizationMatcher> pathMatchers;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        List<AuthorizationMatcher> pathMatchers = new ArrayList<>();

        pathMatchers.add(new AuthorizationMatcher("/operator/home.jsp", UserRole.OPERATOR));
        pathMatchers.add(new AuthorizationMatcher("/operator/create.jsp", UserRole.OPERATOR));
        pathMatchers.add(new AuthorizationMatcher("/subscriber/home.jsp", UserRole.SUBSCRIBER));
        pathMatchers.add(new AuthorizationMatcher("/subscriber/all.jsp", UserRole.OPERATOR));
        pathMatchers.add(new AuthorizationMatcher("subscriber/infobyid.jsp", UserRole.OPERATOR, UserRole.SUBSCRIBER));
        pathMatchers.add(new AuthorizationMatcher("/subscriber/lock.jsp", UserRole.SUBSCRIBER));
        pathMatchers.add(new AuthorizationMatcher("/product/all.jsp", UserRole.OPERATOR, UserRole.OPERATOR));
        pathMatchers.add(new AuthorizationMatcher("/rate/add.jsp", UserRole.OPERATOR));
        pathMatchers.add(new AuthorizationMatcher("/rate/byid.jsp", UserRole.OPERATOR, UserRole.SUBSCRIBER));
        pathMatchers.add(new AuthorizationMatcher("/rate/byproduct.jsp", UserRole.OPERATOR, UserRole.SUBSCRIBER));

        this.pathMatchers = pathMatchers;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String pathWithoutContext = getPathWithoutContext(request);

        Boolean access = false;

        for (AuthorizationMatcher a : pathMatchers) {
            if(a.pathMatch(pathWithoutContext)){
                hasRole(a,request);
            }
        }

        Boolean hasAccess = pathMatchers.stream()
                .filter(authorizationPathMatcher -> authorizationPathMatcher.pathMatch(pathWithoutContext))
                .findFirst()
                .map(authorizationPathMatcher -> hasRole(authorizationPathMatcher, request))
                .orElse(true);

        if (hasAccess) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error/forbiden.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void destroy() {
        pathMatchers.clear();
    }

    private String getPathWithoutContext(HttpServletRequest httpServletRequest) {
        int contextPathLength = httpServletRequest.getContextPath().length();
        return httpServletRequest.getRequestURI().substring(contextPathLength);
    }

    private boolean hasRole(AuthorizationMatcher authorizationMatcher, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && authorizationMatcher.hasRole((User) session.getAttribute("user"));

    }
}
