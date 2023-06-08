package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.AuthenticationReqDTO;
import com.tfg.sprintplannerapi.dto.TokenInfoDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.error.BadInputException;
import com.tfg.sprintplannerapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserBO extends BaseBO<User, Long, UserDTO, UserRepository> {
    @Autowired private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserBO.class);
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtilBO jwtUtilBO;
    @Autowired private ImageBO imageBO;
    @Autowired UserDetailsService usuarioDetailsService;

    public User createUser (UserDTO userDTO) throws NoSuchMethodException {
        User newUser = userDTO.obtainFromDomain();
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(newUser);
    }


    /** Función que autentica un usuario por el username y la contraseña y devuelve un token */
    public TokenInfoDTO authenticate (AuthenticationReqDTO authenticationReqDTO) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationReqDTO.getEmail(),
                            authenticationReqDTO.getPassword()));
            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReqDTO.getEmail());
            final String jwt = jwtUtilBO.generateToken(userDetails);
            TokenInfoDTO tokenInfo = new TokenInfoDTO(jwt);
            logger.info("token info", tokenInfo.getJwtToken());
            return tokenInfo;
        }catch (Exception e){
            throw new BadInputException();
        }
    }

    public Boolean updateImage (MultipartFile image) {
        User userLogged = findUserLogged();
        String imageSaved = imageBO.uploadImage(image) ;

        if (imageSaved != null) {
            userLogged.setAvatar(imageSaved);
            repositorio.save(userLogged);
            return true;
        }
        return false;
    }


    public void deleteUser(){
        User userlogged = findUserLogged();
        userlogged.setDeleted(true);
        repositorio.save(userlogged);
    }

    public TokenInfoDTO updateUser(UserDTO dto)  {
        User userlogged = findUserLogged();
        TokenInfoDTO token = null;

        //if(dto.getEmail() != null || !dto.getEmail().equals("") ) { userlogged.setEmail(dto.getEmail());}
        if(dto.getUsername() != null) { userlogged.setUsername(dto.getUsername());}
        if(dto.getPassword() != null) {

            userlogged.setPassword(passwordEncoder.encode(dto.getPassword()));
            token = authenticate(new AuthenticationReqDTO(userlogged.getEmail(), dto.getPassword()));
        }

        repositorio.save(userlogged);

        return token;
    }

    @Transactional
    public User findUserLogged (){
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        User userLogged = userRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);

        return userLogged;
    }

    public UserDTO findUserLoggedDTO (){
        User user = findUserLogged();
        UserDTO userDTO = new UserDTO();
        userDTO.loadFromDomain(user);
        return userDTO;
    }



}
