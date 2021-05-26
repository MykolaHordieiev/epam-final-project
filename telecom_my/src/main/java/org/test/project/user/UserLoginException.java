package org.test.project.user;

import org.test.project.infra.web.MyException;

public class UserLoginException extends RuntimeException implements MyException {

    UserLoginException(String message){
        super(message);
    }
}
