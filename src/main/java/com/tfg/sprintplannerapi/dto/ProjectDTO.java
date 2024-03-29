package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO extends BaseDTO<Project> {
    private String title;
    private String description;
    private State state = State.OPEN;
    private List<String> teamEmails ;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Madrid")
    private Date updateDate;
    private String directorEmail;
    private String createdBy;
    private Boolean deleted;
    private List<SprintDTO> sprints;

    @Override
    public void loadFromDomain(final Project project) {
        super.loadFromDomain(project);
        if(project.getDirector() != null){
            directorEmail = project.getDirector().getEmail();
        }
        if(!project.getTeam().isEmpty()){
            teamEmails = new ArrayList<>();
            project.getTeam().forEach(member->{
                teamEmails.add(member.getEmail());
            });
        }
    }
}
