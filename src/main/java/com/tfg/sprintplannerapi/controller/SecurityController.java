package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.UserBO;
import com.tfg.sprintplannerapi.dto.AuthenticationReqDTO;
import com.tfg.sprintplannerapi.dto.TokenInfoDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public")
public class SecurityController {
    @Autowired
    private UserBO userBO;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * Ruta pública que registra a un usuario y le devuelve un token
     * @param newuser UserDTO
     * @return código 200 + token ó  400
     */
    @PostMapping("/register")
    public ResponseEntity<?> postUser(@RequestBody UserDTO newuser) {
        User saved = userBO.createUser(newuser);

        if(saved != null){
            TokenInfoDTO token = userBO.authenticate(new AuthenticationReqDTO(newuser.getEmail(), newuser.getPassword()));
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Función que devuelve un nuevo token a un usuario registrado
     * @param authenticationReq AuthenticationReqDTO
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> getToken(@RequestBody AuthenticationReqDTO authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getEmail());
        TokenInfoDTO token = userBO.authenticate(authenticationReq);
        return (token == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(token);
    }
}
