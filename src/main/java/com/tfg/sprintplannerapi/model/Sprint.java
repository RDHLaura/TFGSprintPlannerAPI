package com.tfg.sprintplannerapi.model;


import com.sun.istack.NotNull;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sprints")
public class Sprint extends Audit{
    private Date date = new Date();
    private static final long serialVersionUID = -379888739448877670L;
    @Column(name = "name", nullable = false, length = 50)
    @NotNull
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Date endDate = date.from(date.toInstant().plus(15, ChronoUnit.DAYS ));

    @Column(nullable = false)
    private State state = State.OPEN;

    @ManyToOne
    @JoinColumn(name="projects_id", nullable = false)
    private Project project;
}
