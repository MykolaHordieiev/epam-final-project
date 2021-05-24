package org.test.project.operator;

import lombok.NoArgsConstructor;
import org.test.project.user.User;
import org.test.project.user.UserRole;

import java.util.Locale;

@NoArgsConstructor
public class Operator extends User {

    public Operator(Long id, String login, String password, UserRole userRole) {
        super(id, login, password, userRole);
    }
}
