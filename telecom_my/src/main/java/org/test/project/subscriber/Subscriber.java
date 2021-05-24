package org.test.project.subscriber;

import lombok.Data;
import org.test.project.User.User;
import org.test.project.User.UserRole;

import java.util.Locale;

@Data
public class Subscriber extends User {

    private double balance;
    private boolean lock;

    public Subscriber() {
        this.setUserRole(UserRole.SUBSCRIBER);
    }

    public Subscriber(Long id, String login, String password, double balance, boolean lock, Locale locale) {
        super(id, login, password, UserRole.SUBSCRIBER, locale);
        this.balance = balance;
        this.lock = lock;
    }

    @Override
    public String toString() {
        return this.getLogin();
    }
}
