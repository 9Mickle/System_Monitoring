package com.epam.system_monitoring.exception.data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughDataException extends RuntimeException{
    public NotEnoughDataException(String message) {
        super(message);
    }
}
