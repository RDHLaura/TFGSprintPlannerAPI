package com.tfg.sprintplannerapi.dao;

import com.tfg.sprintplannerapi.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByDeletedIsFalseAndSprintId(Pageable pageable, Long id);
    Page<Task> findAllByDeletedIsFalseAndSprintIdAndAssignedToId(Pageable pageable, Long idSprint, Long idUser);
}
