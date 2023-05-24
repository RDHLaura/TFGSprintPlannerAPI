package com.tfg.sprintplannerapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="projects")
public class Project extends Audit{
    private static final long serialVersionUID = 2670113796662909312L;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private State state = State.OPEN;

    @ManyToOne
    @JoinColumn(name="users_id")
    private User director = null;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "participations",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    List<User> team = new ArrayList<>();
}
