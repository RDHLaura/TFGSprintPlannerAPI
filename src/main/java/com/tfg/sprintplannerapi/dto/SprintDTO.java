package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.Sprint;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintDTO extends BaseDTO<Sprint>{
    private String name;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Madrid")
    private Date endDate;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Madrid")
    private Date createDate;
    private State state;
    private Long idProject;

    @Override
    public void loadFromDomain(Sprint sprint) {
        super.loadFromDomain(sprint);
        if(sprint.getProject() != null){
            setIdProject(sprint.getProject().getId());
        }

    }


}
