package com.epam.system_monitoring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//todo поменять статус.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TitleAlreadyExistException extends RuntimeException{
    public TitleAlreadyExistException(String message) {
        super(message);
    }
}
