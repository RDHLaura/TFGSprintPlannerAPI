package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.JwtUtilBO;
import com.tfg.sprintplannerapi.bo.UserBO;
import com.tfg.sprintplannerapi.dto.AutheticationReqDTO;
import com.tfg.sprintplannerapi.dto.TokenInfoDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilBO jwtUtilBO;

    @Autowired
    private UserBO userBO;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        logger.info("Datos del Usuario: {}", auth.getPrincipal());
        logger.info("Datos de los Permisos {}", auth.getAuthorities());
        logger.info("Esta autenticado {}", auth.isAuthenticated());

        List<Usuario> listUser = userBO.findAll();
        return listUser.isEmpty()?
            ResponseEntity.noContent().build():
            ResponseEntity.ok(listUser);
    }

    @PostMapping("/public/user")
    public ResponseEntity<?> getUsers(@RequestBody UserDTO newuser) {
        Usuario saved = userBO.createUser(newuser);
        /** si el usuario se crea correctamente, se autentica y se envia el token */
        if(saved != null){
            TokenInfoDTO token = authenticate(new AutheticationReqDTO(newuser.getUsername(), newuser.getPassword()));
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().build();

    }


    //TODO moverlo al service
    /**Función que autentica un usuario por el username y la contraseña y de devuelve un token*/
    private TokenInfoDTO authenticate (AutheticationReqDTO autheticationReqDTO) {
        logger.info("Autenticando al usuario {}", autheticationReqDTO.getUsuario(), autheticationReqDTO.getClave());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(autheticationReqDTO.getUsuario(),
                        autheticationReqDTO.getClave()));
        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(autheticationReqDTO.getUsuario());

        final String jwt = jwtUtilBO.generateToken(userDetails);
        TokenInfoDTO tokenInfo = new TokenInfoDTO(jwt);

        logger.info("token info", tokenInfo.getJwtToken());
        return tokenInfo;
    }

}
