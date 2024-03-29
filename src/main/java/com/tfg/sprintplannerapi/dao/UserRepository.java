package com.tfg.sprintplannerapi.dao;

import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    User findByUsernameIgnoreCase(String username);


}
