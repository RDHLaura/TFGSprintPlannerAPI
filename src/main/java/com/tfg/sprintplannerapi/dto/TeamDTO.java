package com.tfg.sprintplannerapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.utils.ListMapper;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TeamDTO extends BaseDTO<Project> {
    private List<UserDTO> team;

    @Override
    public void loadFromDomain(Project entity) {
        super.loadFromDomain(entity);
        if(!entity.getTeam().isEmpty()){
            try {
                setTeam(ListMapper.map(entity.getTeam(), UserDTO.class));
            } catch (NoSuchMethodException |
                     InvocationTargetException |
                     InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
