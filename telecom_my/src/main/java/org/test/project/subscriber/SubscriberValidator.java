package org.test.project.subscriber;

import org.test.project.subscriber.dto.SubscriberCreateDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubscriberValidator {

    private final static String REGEX_LOGIN = "(^[a-zа-я0-9_-]{3,26}$)|(^[a-z_0-9]{0,20}@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,6}$)";
    private final static String REGEX_PASSWORD = "^(?=.*[A-ZА-яa-zа-я])(?=.*\\d)(?=.*[@$!%*#?&])[A-ZА-яa-zа-я\\d@$!%*#?&]{8,30}$";

    public String checkEmptyLogin(String login) {
        if (login.equals("")) {
            throw new SubscriberException("Login cannot be empty. Please enter subscriber`s login.");
        }
        return login;
    }

    public String checkEmptyPassword(String password) {
        if (password.equals("")) {
            throw new SubscriberException("Password cannot be empty. Please enter subscriber`s password.");
        }
        return password;
    }

    public SubscriberCreateDTO checkValidLoginPassword(SubscriberCreateDTO subscriberCreateDTO) {
        checkEmptyLogin(subscriberCreateDTO.getLogin());
        checkEmptyPassword(subscriberCreateDTO.getPassword());
        validateLogin(subscriberCreateDTO.getLogin());
        validatePassword(subscriberCreateDTO.getPassword());
        return subscriberCreateDTO;
    }

    public void validateLogin(String login) {
        Pattern pattern = Pattern.compile(REGEX_LOGIN);
        Matcher matcher = pattern.matcher(login);
        if (!matcher.matches()) {
            throw new SubscriberException("login: " + login + " is not valid");
        }
    }

    public void validatePassword(String password) {
        Pattern pattern = Pattern.compile(REGEX_PASSWORD);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new SubscriberException("Password must contain at least: " +
                    "1 lowercase alphabetical character; " +
                    "1 uppercase alphabetical character; " +
                    "1 numeric character; " +
                    "1 special character" +
                    "Password length must be min 8.");
        }
    }

    public Double checkEntryNumber(String stringAmount) {
        if (stringAmount.equals("")) {
            throw new SubscriberException("Field with amount cannot be empty." +
                    " Please enter amount for replenish your balance.");
        }
        Double amount = Double.parseDouble(stringAmount);
        if (amount < 0) {
            throw new SubscriberException("amount cannot be < 0");
        }
        return amount;
    }
}
