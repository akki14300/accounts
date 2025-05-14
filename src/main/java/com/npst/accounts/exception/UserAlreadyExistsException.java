package com.npst.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s is already registered with the given input data %s : '%s'", resourceName, fieldName, fieldValue));

    }
}
