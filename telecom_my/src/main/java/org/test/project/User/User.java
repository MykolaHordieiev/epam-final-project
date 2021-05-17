package org.test.project.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {

    private Long id;
    private String login;
    private String password;
    private UserRole userRole;
}
