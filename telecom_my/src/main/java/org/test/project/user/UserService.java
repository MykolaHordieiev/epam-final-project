package org.test.project.user;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RequiredArgsConstructor
public class UserService {

    private static Logger log = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    public User loginUser(String login, String password) {
        User user = userRepository.getUserByLogin(login)
                .orElseThrow(() -> new UserLoginException("user by login not found"));
        log.info(password);
        if (!user.getPassword().equals(password)) {
            throw new UserLoginException("password no match");
        }
        return user;
    }
}
