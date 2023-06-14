package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.SprintRepository;
import com.tfg.sprintplannerapi.dao.TaskRepository;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.TaskDTO;
import com.tfg.sprintplannerapi.dto.UserDTO;
import com.tfg.sprintplannerapi.error.NotFoundException;
import com.tfg.sprintplannerapi.error.UnauthorizedAccessException;
import com.tfg.sprintplannerapi.model.*;
import com.tfg.sprintplannerapi.model.states.StateTask;
import com.tfg.sprintplannerapi.utils.ListMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskBO extends BaseBO<Task, Long, TaskDTO, TaskRepository> {
    @Autowired private UserBO userBO;
    @Autowired private SprintRepository sprintRepository;
    @Autowired private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    public Page<TaskDTO> findAllTaskPage(Pageable pageable, Long idSprint){
        Sprint sprint = sprintRepository
                .findById(idSprint)
                .orElseThrow(NotFoundException::new);
        User userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, sprint.getProject());
        if(!roles.contains(Rol.PARTICIPANTE) ){
            throw new UnauthorizedAccessException();
        }

        Page <Task> taskList;
        if(roles.contains(Rol.DIRECTOR) || roles.contains(Rol.CREADOR)){
            taskList = repositorio.findAllByDeletedIsFalseAndSprintId(pageable, idSprint);
        }else{
            taskList = repositorio.findAllByDeletedIsFalseAndSprintIdAndAssignedToId(pageable, idSprint, userLogged.getId());
        }

        List<TaskDTO> taskDTOList;
        try {
            taskDTOList = ListMapper.map(taskList.getContent(), TaskDTO.class);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return new PageImpl<>(taskDTOList, pageable, taskList.getTotalElements());
    }

   public TaskDTO createTask (Long idSprint, TaskDTO dtoTask) throws NoSuchMethodException {
       Sprint sprint    = sprintRepository.findById(idSprint).orElseThrow(NotFoundException::new);
       User userLogged  = userBO.findUserLogged();
       List<Rol> roles  = Rol.getRolUser(userLogged, sprint.getProject());
       if(!roles.contains(Rol.DIRECTOR) ){
           throw new UnauthorizedAccessException();
       }
       dtoTask.setId(null);
       User user = userRepository.findByEmailIgnoreCase(dtoTask.getAssignedTo().getEmail()).orElse(null);
       UserDTO userDTO = new UserDTO();
       userDTO.setId(user.getId());
       dtoTask.setAssignedTo(userDTO);
       Task task = dtoTask.obtainFromDomain();

       if(user != null){
           task.setAssignedTo(user);
       }

       repositorio.save(task);
       TaskDTO taskDTO = new TaskDTO();
       taskDTO.loadFromDomain(task);
       return taskDTO;
   }

    public TaskDTO updateTask(Long idTask, TaskDTO dtoTask) {
        Task task = getTask(idTask);
        User userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, task.getSprint().getProject());
        if(!roles.contains(Rol.DIRECTOR) ){
            throw new UnauthorizedAccessException();
        }
        if(dtoTask.getName()        != null){task.setName(dtoTask.getName());}
        if(dtoTask.getDescription() != null){task.setDescription(dtoTask.getDescription());}
        if(dtoTask.getDeadline()    != null){task.setDeadline(dtoTask.getDeadline());}
        if(dtoTask.getAssignedTo()  != null){
            User user = userRepository.findByEmailIgnoreCase(dtoTask.getAssignedTo().getEmail()).orElse(null);
            if(user != null){
                task.setAssignedTo(user);
            }

        }

        repositorio.save(task);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.loadFromDomain(task);
        return taskDTO;
    }

    public TaskDTO updateStateTask(Long idTask, StateTask state) {
        Task task = getTask(idTask);
        User userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, task.getSprint().getProject());
        if(!roles.contains(Rol.DIRECTOR) && task.getAssignedTo().getId() != userLogged.getId()){
            throw new UnauthorizedAccessException();
        }
        if(state != null){ task.setState(state); }
        repositorio.save(task);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.loadFromDomain(task);
        return taskDTO;
    }

    public void deleteTask(Long idTask)  {
        Task task = getTask(idTask);
        User userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, task.getSprint().getProject());
        if(!roles.contains(Rol.DIRECTOR)){
            throw new UnauthorizedAccessException();
        }
        taskRepository.delete(task);
    }

    public Task getTask (Long id){
        Task task = taskRepository.findById(id).orElseThrow(NotFoundException::new);
        Hibernate.initialize(task);
        return task;
    }
}
