package com.tfg.sprintplannerapi.controller;


import com.tfg.sprintplannerapi.bo.ImageBO;
import com.tfg.sprintplannerapi.bo.UserBO;
import com.tfg.sprintplannerapi.dto.AuthenticationReqDTO;
import com.tfg.sprintplannerapi.dto.TokenInfoDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired private UserBO userBO;
    @Autowired private ImageBO imageBO;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Acceso a autenticados, lista la lista de usuarios
     * @return 200 + lista de usuarios ó 204
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {

        List<User> listUser = userBO.findAll();
        return listUser.isEmpty()?
                ResponseEntity.noContent().build():
                ResponseEntity.ok(listUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getOneUser() {
        User user = userBO.findUserLogged();
        //TODO mapear
        UserDTO userDTO = new UserDTO(user.getEmail(), user.getUsername(), null);
        return (user == null)?
                ResponseEntity.notFound().build():
                ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/avatar")
    public ResponseEntity<Resource> getUser() {
        User user = userBO.findUserLogged();
        String avatarName = user.getAvatar();
        Resource image = imageBO.loadImage(avatarName);

        if (image.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Cambia el tipo de contenido según el formato de la imagen
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatarName + "\"")
                    .body(image);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Actualiza el avatar del usuario
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {

        Boolean updated = userBO.updateImage(file);
        return (updated) ?
                ResponseEntity.ok("imagen actualizada") :
                ResponseEntity.badRequest().build();
    }





    /* Para mostrar el formulario de prueba para subir imagenes. //TODO borrar*/
    @GetMapping("/user/{id}/uploadimage") public String displayUploadForm() {
        return "/imageupload/index.html";
    }

}
