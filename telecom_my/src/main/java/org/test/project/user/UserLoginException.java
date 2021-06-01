package org.test.project.user;

import org.test.project.infra.exception.TelecomException;

public class UserLoginException extends TelecomException {

    UserLoginException(String message){
        super(message);
    }
}
