package com.tfg.sprintplannerapi.controller;


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

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("")
public class UserController {
    @Autowired private UserBO userBO;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * Ruta pública que registra a un usuario y le devuelve un token
     * @param newuser UserDTO
     * @return código 200 + token ó  400
     */
    @PostMapping("/public/register")
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
    @PostMapping("/public/authenticate")
    public ResponseEntity<?> getToken(@RequestBody AuthenticationReqDTO authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getEmail());
        TokenInfoDTO token = userBO.authenticate(authenticationReq);
        return (token == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(token);
    }

    /**
     * Acceso a autenticados, lista la lista de usuarios
     * @return 200 + lista de usuarios ó 204
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {

        List<User> listUser = userBO.findAll();
        return listUser.isEmpty()?
                ResponseEntity.noContent().build():
                ResponseEntity.ok(listUser);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getOneUser(@RequestParam Long id) {
        //TODO devolver DTO
        User user = userBO.findById(id).orElse(null);
        return (user == null)?
                ResponseEntity.notFound().build():
                ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}/avatar")
    public ResponseEntity<Resource> getUser(@PathVariable Long id) {
        User user = userBO.findById(id).orElse(null);
        String avatarName = user.getAvatar();
        if(avatarName != null){
            Path imagePath = Paths.get("src//main//resources//static//uploads//" + avatarName);
            Resource imageResource;
            try {
                imageResource = new FileSystemResource(imagePath);
                if (imageResource.exists()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG) // Cambia el tipo de contenido según el formato de la imagen
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatarName + "\"")
                            .body(imageResource);
                }
            } catch (Exception e) {
                // Manejo del error
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Actualiza el avatar del usuario
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/user/avatar")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

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
