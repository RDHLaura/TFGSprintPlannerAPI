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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

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
        User newUser = new User();
        //TODO configurar mapper y mapear automaticamente
        newUser.setEmail(userDTO.getEmail());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(newUser);
    }




    /** Función que autentica un usuario por el username y la contraseña y devuelve un token */
    public TokenInfoDTO authenticate (AuthenticationReqDTO authenticationReqDTO) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationReqDTO.getEmail(),
                            authenticationReqDTO.getClave()));
            final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReqDTO.getEmail());
            final String jwt = jwtUtilBO.generateToken(userDetails);
            TokenInfoDTO tokenInfo = new TokenInfoDTO(jwt);
            logger.info("token info", tokenInfo.getJwtToken());

            return tokenInfo;
        }catch (Exception e){
            return null;
        }
    }
    public Boolean updateImage (MultipartFile image) {
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        User userLogged = userRepository.findByEmailIgnoreCase(auth.getName());
        String imageSaved = uploadImage(image) ;

        if (imageSaved != null) {
            userLogged.setAvatar(imageSaved);
            return true;
        }
        return false;
    }
    /** Carga la imagen y devuelve su ruta */
    public String uploadImage( MultipartFile image){
        if(!image.isEmpty()){
            Path directoryImage = Paths.get("src//main//resources//static//uploads");
            String absoluteRoute = directoryImage.toFile().getAbsolutePath();
            try{
                byte[] byteImg = image.getBytes();
                String uniqueFileName = generateUniqueFileName(image.getOriginalFilename());

                Path imageRoute = Paths.get(absoluteRoute + "//" + uniqueFileName);
                Files.write(imageRoute,byteImg );

                return uniqueFileName;
            }catch (IOException e){
                return null;
            }
        }
        return null;
    }


    private String generateUniqueFileName(String originalFilename) {
        long timestamp = System.currentTimeMillis();
        String uniqueId = UUID.randomUUID().toString();   // Generar un identificador único
        String fileExtension = extractFileExtension(originalFilename);

        String uniqueFileName = timestamp + "-" + uniqueId + fileExtension;

        return uniqueFileName;
    }

    private String extractFileExtension(String filename) {
        int extensionIndex = filename.lastIndexOf('.');
        if (extensionIndex > 0 && extensionIndex < filename.length() - 1) {
            return filename.substring(extensionIndex);
        }
        return "";
    }
}
