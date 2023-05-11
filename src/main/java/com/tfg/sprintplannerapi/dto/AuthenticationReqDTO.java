package com.tfg.sprintplannerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthenticationReqDTO implements Serializable {

    private static final long serialVersionUID = 969448392357624487L;
    private String email;
    private String clave;
}
