package org.test.project.user;

import org.junit.Test;

public class UserRepositoryTest {

    private final UserRepository userRepository;

    public UserRepositoryTest( UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void doTest() {
        String login = "a";
        userRepository.getUserByLogin(login);
    }
}
