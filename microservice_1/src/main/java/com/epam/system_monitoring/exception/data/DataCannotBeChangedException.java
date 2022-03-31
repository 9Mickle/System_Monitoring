package com.epam.system_monitoring.exception.data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataCannotBeChangedException extends RuntimeException{
    public DataCannotBeChangedException(String message) {
        super(message);
    }
}
