package com.tfg.sprintplannerapi.dao;

import com.tfg.sprintplannerapi.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findAllByProjectIdOrderByCreateDateDesc(Long projectId);
}
