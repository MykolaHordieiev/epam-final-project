package org.test.project.infra.auth;

import lombok.Value;
import org.test.project.User.User;
import org.test.project.User.UserRole;

import java.util.Arrays;
import java.util.List;

@Value
public class AuthorizationMatcher {

    String pathRegex;
    List<UserRole> accessRoles;

    public AuthorizationMatcher(String pathRegex, UserRole... roles) {
        this.pathRegex = pathRegex;
        this.accessRoles = Arrays.asList(roles);
    }

    public boolean pathMatch(String path) {
        return path.matches(pathRegex);
    }

    public boolean hasRole(User user) {
        return user != null && accessRoles.contains(user.getUserRole());
    }
}
