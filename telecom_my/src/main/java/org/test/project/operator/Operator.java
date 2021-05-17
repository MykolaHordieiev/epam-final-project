package org.test.project.operator;

import lombok.NoArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;

@NoArgsConstructor
public class Operator extends User {

    public Operator(Long id, String login, String password, UserRole userRole) {
        super(id, login, password, userRole);
    }
}
