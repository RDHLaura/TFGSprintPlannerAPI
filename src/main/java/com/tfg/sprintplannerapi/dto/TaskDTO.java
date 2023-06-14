package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tfg.sprintplannerapi.model.Task;
import com.tfg.sprintplannerapi.model.states.StateTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO extends BaseDTO<Task> {
    private String name;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Madrid")
    private Date deadline;
    private StateTask state = StateTask.SINEMPEZAR;
    private SprintDTO sprint ;
    private UserDTO assignedTo;

    @Override
    public void loadFromDomain(Task task) {
        super.loadFromDomain(task);
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.loadFromDomain(task.getSprint());
        setSprint(sprintDTO);
        assignedTo.loadFromDomain(task.getAssignedTo());
        sprint.loadFromDomain(task.getSprint());
    }

    @Override
    public Task obtainFromDomain() throws NoSuchMethodException {
        Task task = super.obtainFromDomain();
        task.setAssignedTo(assignedTo.obtainFromDomain());
        task.setSprint(sprint.obtainFromDomain());
        return task;
    }
}
