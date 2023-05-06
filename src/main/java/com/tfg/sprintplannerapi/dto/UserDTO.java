package com.tfg.sprintplannerapi.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String email;
    private String username;
    private String password;
    private String avatar = null;
}
