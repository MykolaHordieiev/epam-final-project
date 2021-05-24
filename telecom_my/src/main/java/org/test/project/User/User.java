package org.test.project.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {

    private Long id;
    private String login;
    private String password;
    private UserRole userRole;
    private Locale locale;
}
