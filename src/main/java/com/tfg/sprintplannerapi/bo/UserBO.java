package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserBO extends BaseBO<User, Long, UserRepository> {
    @Autowired private ModelMapper modelMapper;
    @Autowired private UserRepository userRepository;

    @Autowired PasswordEncoder passwordEncoder;


    public User createUser (UserDTO userDTO) {
        User usuario = new User();

        usuario.setEmail(userDTO.getEmail());
        usuario.setUsername(userDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        usuario.setAvatar(usuario.getAvatar());

        return userRepository.save(usuario);
    }
}
