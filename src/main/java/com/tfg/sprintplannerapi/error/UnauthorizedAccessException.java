package com.tfg.sprintplannerapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException() {super("You do not have the appropriate permissions to perform that action");}
}
