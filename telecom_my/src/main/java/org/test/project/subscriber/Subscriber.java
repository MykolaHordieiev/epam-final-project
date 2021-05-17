package org.test.project.subscriber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;

@Data
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
