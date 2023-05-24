package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO extends BaseDTO<Project> {
    private String title;
    private String description;
    private State state;
    private String directorEmail;

    @Override
    public void loadFromDomain(final Project project) {
        super.loadFromDomain(project);
        if(project.getDirector() != null){
            directorEmail = project.getDirector().getEmail();
        }
    }
}
