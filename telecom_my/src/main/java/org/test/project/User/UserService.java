package org.test.project.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.project.subscriber.Subscriber;


@RequiredArgsConstructor
public class UserService {

    private static Logger log = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;


    public User loginUser(String login, String password) {
        System.out.println("method loginUser");
        User user = userRepository.getUserByLogin(login)
                .orElseThrow(() -> new UserLoginException("user by login not found"));
        log.info(password);
        System.out.println(password);
        if (!user.getPassword().equals(password)) {
            throw new UserLoginException("password no match");
        }
        System.out.println(user);
        return user;
    }



}
