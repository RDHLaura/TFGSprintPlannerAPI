package com.tfg.sprintplannerapi.model;


import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.model.states.StateTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends Audit{
    private Date date = new Date();
    private static final long serialVersionUID = 5241180616920111693L;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Date deadline = date.from(date.toInstant().plus(2, ChronoUnit.DAYS ));

    @Column(nullable = false)
    private StateTask state = StateTask.SINEMPEZAR;

    @ManyToOne
    @JoinColumn(name="sprints_id", nullable = false)
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name="users_id", nullable = false)
    private User assignedTo;


}
