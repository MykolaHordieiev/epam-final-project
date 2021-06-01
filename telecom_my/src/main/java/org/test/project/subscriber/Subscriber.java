package org.test.project.subscriber;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.test.project.user.User;
import org.test.project.user.UserRole;

import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = true)
public class Subscriber extends User {

    private double balance;
    private boolean lock;

    public Subscriber() {
        this.setUserRole(UserRole.SUBSCRIBER);
    }

    public Subscriber(Long id, String login, String password, double balance, boolean lock) {
        super(id, login, password, UserRole.SUBSCRIBER);
        this.balance = balance;
        this.lock = lock;
    }
}
