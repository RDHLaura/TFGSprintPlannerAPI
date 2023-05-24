package com.tfg.sprintplannerapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    public NotFoundException() {super("The specified item could not be found.");}
    public NotFoundException(Long id) {super("The item with ID " + id + " could not be found.");}

}
