package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.ImageBO;
import com.tfg.sprintplannerapi.bo.UserBO;
import com.tfg.sprintplannerapi.dto.AuthenticationReqDTO;
import com.tfg.sprintplannerapi.dto.TokenInfoDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.model.User;
import com.tfg.sprintplannerapi.security.PermissionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3003") //TODO borrar y testear desde el front
@RestController
@RequestMapping("")
public class UserController {
    @Autowired private UserBO userBO;
    @Autowired private ImageBO imageBO;
    @Autowired private PermissionFilter<User> permissionFilter;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    /**
     * Read all users
     * @return 200 + lista de usuarios ó 204
     */
    @GetMapping("/user")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @PageableDefault(page = 0, size = 4)
            @SortDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable
            ) {
        Page<UserDTO> listUser = userBO.findAll(pageable);

        return ResponseEntity.ok(listUser);
    }
    @GetMapping("/user/all")
    public ResponseEntity<List<UserDTO>> getAllUsersList() {
        List<UserDTO> listUser = userBO.findAllList();

        return ResponseEntity.ok(listUser);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getUserLogged() {
        UserDTO userDTO = userBO.findUserLoggedDTO();
        return (userDTO == null)?
                ResponseEntity.notFound().build():
                ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getOneUser(@PathVariable Long id) {
        UserDTO userDTO = userBO.findOneDTO(id);
        return ResponseEntity.ok().body(userDTO);
    }
    @GetMapping("/user/avatar")
    public ResponseEntity<Resource> getAvatar() {
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
    @PostMapping("/user/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("image") MultipartFile file) {

        Boolean updated = userBO.updateImage(file);
        return (updated) ?
                ResponseEntity.ok("imagen actualizada") :
                ResponseEntity.badRequest().build();
    }


    /**
     * Ruta pública que registra a un usuario y le devuelve un token
     * @param newuser UserDTO
     * @return código 200 + token ó  400
     */
    @PostMapping("/public/user/register")
    public ResponseEntity<?> postUser(@RequestBody UserDTO newuser) throws NoSuchMethodException {
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
    @PostMapping("/public/user/authenticate")
    public ResponseEntity<TokenInfoDTO> getToken(@RequestBody AuthenticationReqDTO authenticationReq) {
        LOG.info("Autenticando al usuario {}", authenticationReq.getEmail());
        TokenInfoDTO token = userBO.authenticate(authenticationReq);
        return (token == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(token);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneUser() {
        userBO.deleteUser();
        return ResponseEntity.noContent().build();
    }

  
    @PutMapping("/user/me/update")
    public ResponseEntity<UserDTO> updateUser(UserDTO userDTO) {
        UserDTO updatedUser = userBO.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /* Para mostrar el formulario de prueba para subir imagenes. //TODO borrar*/
    @GetMapping("/user/{id}/uploadimage") public String displayUploadForm() {
        return "/imageupload/index.html";
    }

}
