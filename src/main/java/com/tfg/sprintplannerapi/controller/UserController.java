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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {
    @Autowired
    private UserBO userBO;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Acceso a autenticados, lista la lista de usuarios
     * @return 200 + lista de usuarios ó 204
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> listUser = userBO.findAll();
        return listUser.isEmpty()?
            ResponseEntity.noContent().build():
            ResponseEntity.ok(listUser);
    }

    /**
     * Ruta pública que registra a un usuario y le devuelve un token
     * @param newuser UserDTO
     * @return código 200 + token ó  400
     */
    @PostMapping("/public/register")
    public ResponseEntity<?> getUsers(@RequestBody UserDTO newuser) {
        User saved = userBO.createUser(newuser);
        if(saved != null){
            TokenInfoDTO token = userBO.authenticate(new AuthenticationReqDTO(newuser.getUsername(), newuser.getPassword()));
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Función que devuelve un nuevo token a un usuario registrado
     * @param authenticationReq AuthenticationReqDTO
     * @return
     */
    @PostMapping("/public/authenticate")
    public ResponseEntity<?> getToken(@RequestBody AuthenticationReqDTO authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getUsuario());
        TokenInfoDTO token = userBO.authenticate(authenticationReq);
        return (token == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(token);
    }


}
