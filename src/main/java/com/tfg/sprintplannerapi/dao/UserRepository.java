package com.tfg.sprintplannerapi.dao;

import com.tfg.sprintplannerapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailIgnoreCase(String email);
    User findByUsernameIgnoreCase(String username);
}
