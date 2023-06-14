package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.sprintplannerapi.model.Sprint;
import com.tfg.sprintplannerapi.model.states.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintPostDTO extends BaseDTO<Sprint>{
    private String name;
    private String description;
    private State state;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Madrid")
    private Date endDate;
    private Long idProject;
    private Boolean deleted;
}
