package com.tfg.sprintplannerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TokenInfoDTO implements Serializable {

    private static final long serialVersionUID = -800563894528333384L;
    private final String jwtToken;

}
