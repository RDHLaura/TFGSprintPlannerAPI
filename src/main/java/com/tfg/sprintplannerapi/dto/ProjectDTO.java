package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.User;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO extends BaseDTO<Project> {
    private String title;
    private String description;
    private State state;
    private String director;


    @Override
    public void loadFromDomain(final Project project) {

        super.loadFromDomain(project);
        setDirector(project.getDirector().getEmail());
    }
}
