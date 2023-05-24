package com.tfg.sprintplannerapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MappingException extends RuntimeException{
    public MappingException() {super("An error occurred during the mapping of the data.");}
}
