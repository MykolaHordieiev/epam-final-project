package org.test.project.operator;

import lombok.NoArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;

import java.util.Locale;

@NoArgsConstructor
public class Operator extends User {

    public Operator(Long id, String login, String password, UserRole userRole, Locale locale) {
        super(id, login, password, userRole, locale);
    }
}
