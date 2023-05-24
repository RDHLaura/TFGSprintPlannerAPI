package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.sprintplannerapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO<User>{
    private static final long serialVersionUID = -6022314144747947986L;
    private String email;
    private String username;
    private String password;

}
