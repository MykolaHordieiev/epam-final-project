package org.test.project.user;


public class UserValidator {

    public String checkEmptyLogin(String login) {
        if (login.equals("")) {
            throw new UserLoginException("Login cannot be empty. Please enter your login.");
        }
        return login;
    }

    public String checkEmptyEntryPassword(String password) {
        if (password.equals("")) {
            throw new UserLoginException("Password cannot be empty. Please enter your password.");
        }
        return password;
    }
}
