package com.tfg.sprintplannerapi.dao;

import com.tfg.sprintplannerapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmailIgnoreCase(String email);
    Usuario findByUsernameIgnoreCase(String username);
}
