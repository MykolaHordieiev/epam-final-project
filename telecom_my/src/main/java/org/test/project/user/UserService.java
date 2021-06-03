package org.test.project.user;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.project.user.dto.UserLoginDTO;


@RequiredArgsConstructor
public class UserService {

    private static Logger log = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    public User loginUser(UserLoginDTO userLoginDTO) {
        log.info("Try enter user: " + userLoginDTO.getLogin());

        User foundUser = userRepository.getUserByLogin(userLoginDTO)
                .orElseThrow(() -> new UserLoginException("user by login not found"));
        if (!foundUser.getPassword().equals(userLoginDTO.getPassword())) {
            throw new UserLoginException("password no match");
        }
        log.info("User: " + userLoginDTO.getLogin() + " was entered");
        return foundUser;
    }
}
