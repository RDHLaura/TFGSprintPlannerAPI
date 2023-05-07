package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.AuthenticationReqDTO;
import com.tfg.sprintplannerapi.dto.TokenInfoDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.model.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserBO extends BaseBO<User, Long, UserRepository> {
    @Autowired private ModelMapper modelMapper;
    @Autowired private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserBO.class);
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtilBO jwtUtilBO;
    @Autowired UserDetailsService usuarioDetailsService;


    public User createUser (UserDTO userDTO) {
        User usuario = new User();

        //TODO configurar mapper y mapear automaticamente
        usuario.setEmail(userDTO.getEmail());
        usuario.setUsername(userDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        usuario.setAvatar(usuario.getAvatar());

        return userRepository.save(usuario);
    }



    /** Función que autentica un usuario por el username y la contraseña y de devuelve un token */
    public TokenInfoDTO authenticate (AuthenticationReqDTO authenticationReqDTO) {
        logger.info("Autenticando al usuario {}", authenticationReqDTO.getUsuario(), authenticationReqDTO.getClave());
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationReqDTO.getUsuario(),
                            authenticationReqDTO.getClave()));
            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReqDTO.getUsuario());

            final String jwt = jwtUtilBO.generateToken(userDetails);
            TokenInfoDTO tokenInfo = new TokenInfoDTO(jwt);
            logger.info("token info", tokenInfo.getJwtToken());

            return tokenInfo;

        }catch (Exception e){
            return null; //TODO crear excepción personalizada para informar que no coincide usuario ó contraseña con los datos enviados
        }


    }
}
