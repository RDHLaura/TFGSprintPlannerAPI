package com.tfg.sprintplannerapi.dto;

import com.tfg.sprintplannerapi.model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationReqDTO extends BaseDTO<Project> {
    private static final long serialVersionUID = 969448392357624487L;
    private String email;
    private String password;
}
