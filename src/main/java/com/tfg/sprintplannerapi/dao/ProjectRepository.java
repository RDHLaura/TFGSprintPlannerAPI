package com.tfg.sprintplannerapi.dao;

import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByDeletedAndTeamContaining(Boolean deleted, User user, Pageable pageable);
}
