package org.test.project.subscriber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubscriberValidator {

    private final static String REGEX_LOGIN = "(^[a-zа-я0-9_-]{3,26}$)|(^[a-z_0-9]{0,20}@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,6}$)";
    private final static String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,30})";

    public Subscriber checkEmptyLogin(Subscriber subscriber) {
        if (subscriber.getLogin().equals("")) {
            throw new SubscriberException("Login cannot be empty. Please enter subscriber`s login.");
        }
        return subscriber;
    }

    public Subscriber checkEmptyPassword(Subscriber subscriber) {
        if (subscriber.getLogin().equals("")) {
            throw new SubscriberException("Password cannot be empty. Please enter subscriber`s password.");
        }
        return subscriber;
    }

    public Subscriber checkValidLoginPassword(Subscriber subscriber) {
        checkEmptyLogin(subscriber);
        checkEmptyPassword(subscriber);
        validateLogin(subscriber);
        validatePassword(subscriber);
        return subscriber;
    }

    public void validateLogin(Subscriber subscriber) {
        Pattern pattern = Pattern.compile(REGEX_LOGIN);
        Matcher matcher = pattern.matcher(subscriber.getLogin());
        if (!matcher.matches()) {
            throw new SubscriberException("login: " + subscriber.getLogin() + " is not valid");
        }
    }

    public void validatePassword(Subscriber subscriber) {
        Pattern pattern = Pattern.compile(REGEX_PASSWORD);
        Matcher matcher = pattern.matcher(subscriber.getPassword());
        if (!matcher.matches()) {
            throw new SubscriberException("Password must contain at least: " +
                    "1 lowercase alphabetical character; " +
                    "1 uppercase alphabetical character; " +
                    "1 numeric character. Password length must be min 8.");
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
