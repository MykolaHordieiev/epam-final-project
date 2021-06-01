package org.test.project.user;


import org.test.project.user.dto.UserLoginDTO;

public class UserValidator {

    private String checkEmptyLogin(String login) {
        if (login.equals("")) {
            throw new UserLoginException("Login cannot be empty. Please enter your login.");
        }
        return login;
    }

    private String checkEmptyEntryPassword(String password) {
        if (password.equals("")) {
            throw new UserLoginException("Password cannot be empty. Please enter your password.");
        }
        return password;
    }

    public UserLoginDTO checkUser(UserLoginDTO loginDTO) {
        checkEmptyLogin(loginDTO.getLogin());
        checkEmptyEntryPassword(loginDTO.getPassword());
        return loginDTO;
    }
}
