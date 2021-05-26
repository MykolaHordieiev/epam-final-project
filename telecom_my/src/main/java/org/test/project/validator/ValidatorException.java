package org.test.project.validator;

import org.test.project.infra.web.MyException;

public class ValidatorException extends RuntimeException implements MyException {

    ValidatorException(String message) {
        super(message);
    }
}
