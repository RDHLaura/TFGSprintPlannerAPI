package com.tfg.sprintplannerapi.model;

import com.tfg.sprintplannerapi.model.states.State;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="projects")
public class Project extends Audit implements Comparable<Project>{
    private static final long serialVersionUID = 2670113796662909312L;

    @Column(nullable = false)
    private String title;

    @Column(length = 500, columnDefinition = "varchar(500) default 'John Snow'")
    private String description;

    @Column(nullable = false)
    private State state = State.OPEN;

    @ManyToOne
    @JoinColumn(name="users_id")
    private User director = null;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "participations",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    List<User> team = new ArrayList<>();


    @Override
    public int compareTo(Project p) {
        if(getUpdateDate() == null || p.getUpdateDate() == null) {
            return 0;
        }
        return getUpdateDate().compareTo(p.getUpdateDate());
    }
}
