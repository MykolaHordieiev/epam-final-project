package org.test.project.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorEntryParameter {

    private final static String REGEX_LOGIN = "(^[a-zа-я0-9_-]{3,26}$)|(^[a-z_0-9]{0,20}@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,6}$)";
    private final static String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,30})";

    public String checkEmptyEntryParameter(String entryParameter, String parameter) {
        if (entryParameter.equals("")) {
            throw new ValidatorException("entry value cannot be empty: " + parameter);
        }
        return entryParameter;
    }

    public void checkEntryNumber(Double entryNumber) {
        if (entryNumber < 0) {
            throw new ValidatorException("entry value cannot be < 0");
        }
    }

    public void validateLogin(String login) {
        Pattern pattern = Pattern.compile(REGEX_LOGIN);
        Matcher matcher = pattern.matcher(login);
        if (!matcher.matches()) {
            throw new ValidatorException("login: " + login + " is not valid");
        }
    }

    public void validatePassword(String password) {
        Pattern pattern = Pattern.compile(REGEX_PASSWORD);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new ValidatorException("Password must contain at least: " +
                    "1 lowercase alphabetical character; " +
                    "1 uppercase alphabetical character; " +
                    "1 numeric character. Password length must be min 8.");
        }
    }
}
