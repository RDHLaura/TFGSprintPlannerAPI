package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class TokenInfoDTO implements Serializable {

    private static final long serialVersionUID = -800563894528333384L;
    private final String jwtToken;

}
