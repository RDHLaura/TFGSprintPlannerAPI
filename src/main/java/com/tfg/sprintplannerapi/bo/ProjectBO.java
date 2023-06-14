package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.ProjectRepository;
import com.tfg.sprintplannerapi.dao.SprintRepository;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.dto.ProjectPostDTO;
import com.tfg.sprintplannerapi.dto.SprintDTO;
import com.tfg.sprintplannerapi.dto.SprintPostDTO;
import com.tfg.sprintplannerapi.error.*;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.Rol;
import com.tfg.sprintplannerapi.model.Sprint;
import com.tfg.sprintplannerapi.model.User;
import com.tfg.sprintplannerapi.utils.ListMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


@Service
@Transactional
public class ProjectBO extends BaseBO <Project, Long, ProjectDTO, ProjectRepository> {

    @Autowired private SprintRepository sprintRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired private UserBO userBO;
    private User userLogged;

    public Page<ProjectDTO> findAllPage(Pageable pageable, Boolean deleted) {
        userLogged = userBO.findUserLogged();
        Page<Project> projects = projectRepository.findAllByDeletedAndTeamContaining(deleted, userLogged, pageable);
        if (userLogged.getProjects().isEmpty()) {
            throw new NoContentException();
        }
        /**CONVIERTO A DTO*/
        List<ProjectDTO> projectsDTO = new ArrayList<>();
        for (Project project : projects.getContent()) {
            ProjectDTO projectDTO = mapProjectWithSprints(project);
            projectsDTO.add(projectDTO);
        }

        return new PageImpl<>(projectsDTO, pageable, projects.getTotalElements());
    }

    public ProjectDTO create(ProjectPostDTO dto) {
        userLogged = userBO.findUserLogged() ;
        List<User> newteam = new ArrayList<>();

        dto.setId(null); //se setea a null por si viene en la request con un id no pise ninguna entidad ya creada
        Project project = null;
        try {
            project = dto.obtainFromDomain(); //Mapeo el dto a entidad
        } catch (NoSuchMethodException e) {
            throw new MappingException();
        }
        if(dto.getDirectorEmail() != null){
            project.setDirector(userRepository
                    .findByEmailIgnoreCase(dto.getDirectorEmail())
                    .orElse(null));
        }

        for (String user: dto.getTeamEmails()){
            User member = userRepository.findByEmailIgnoreCase(user).orElse(null);
            if(member != null && !newteam.contains(member)){
                newteam.add(member);
            }
        }
        //añado el creador al team y al director si es distinto del creador
        newteam.add(userLogged);
        newteam.add(project.getDirector());
        project.setTeam(new ArrayList<>(new HashSet<>(newteam)));
        Project created = save(project);
        ProjectDTO projectDTO = mapProjectWithSprints(project);
        return projectDTO;
    }


    public Project deleteProject(Long id)  {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        Hibernate.initialize(project);
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.CREADOR) && !roles.contains(Rol.DIRECTOR)){
            throw new UnauthorizedAccessException();
        }
        project.setDeleted(!project.getDeleted());
        return projectRepository.save(project);
    }


    public ProjectDTO updateProject(Long id, ProjectDTO dto)  {
        Project project = this.findById(id).orElseThrow(NotFoundException::new);
        userLogged = userBO.findUserLogged();
        Hibernate.initialize(project);

        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.CREADOR) && !roles.contains(Rol.DIRECTOR) ){ throw new UnauthorizedAccessException();}
        if(dto.getTitle() != null){ project.setTitle(dto.getTitle()); }
        if(dto.getDescription() != null){ project.setDescription(dto.getDescription()); }
        if(dto.getState() != null){ project.setState(dto.getState()); }
        if(dto.getDirectorEmail() != null){
            User newDirector = userRepository.findByEmailIgnoreCase(dto.getDirectorEmail()).orElse(null);
            project.setDirector(newDirector);
            //si el nuevo director no está ya en el team se incorpora
            if( !project.getTeam().contains(newDirector)){
                project.getTeam().add(newDirector);
            }
        }
        if(dto.getTeamEmails() != null ){
            if(!dto.getTeamEmails().contains(project.getDirector().getEmail())){
                throw new BadInputException("No puede eliminar al director del equipo, deberá asignar la dirección a otro miembro del equipo primero");
            }
            if(!dto.getTeamEmails().contains(project.getCreatedBy())){
                throw new BadInputException("No se puede eliminar al creador del equipo.");
            }
            List<User> updatedTeam = new ArrayList<>();
            dto.getTeamEmails().forEach(member->{
                updatedTeam.add(userRepository.findByEmailIgnoreCase(member).orElseThrow(BadInputException::new));
            });
            project.setTeam(updatedTeam);
        }
        projectRepository.save(project);

        ProjectDTO projectDTO = mapProjectWithSprints(project);
        return projectDTO;
    }


    public List<SprintDTO> findAllSprintsByProject(Long projectId){
        Project project = this.findById(projectId).orElseThrow(NotFoundException::new);
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if( !roles.contains(Rol.PARTICIPANTE) ){ throw new UnauthorizedAccessException();}

        List<Sprint> sprints = sprintRepository.findAllByProjectIdOrderByCreateDateDesc(projectId);
        if(sprints.isEmpty())
            return null;

        List<SprintDTO> sprintsDTO;
        try {
            sprintsDTO = ListMapper.map(sprints, SprintDTO.class);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return sprintsDTO;
    }


    public ProjectDTO createSprint (SprintPostDTO dto){
        Project project = sprintGetProject(dto.getIdProject());

        dto.setId(null); //se setea a null por si viene en la request con un id no pise ninguna entidad ya creada
        Sprint sprint;
        try {
            sprint = dto.obtainFromDomain(); //Mapeo el dto a entidad
            sprint.setProject(project);
        } catch (NoSuchMethodException e) {
            throw new MappingException();
        }
        sprintRepository.save(sprint);
        //devuelvo un dto del proyecto con los sprints
        ProjectDTO projectDTO = mapProjectWithSprints(project);
        return projectDTO;
    }

    public ProjectDTO updateSprint (Long id, SprintPostDTO dto){
        Sprint sprint = sprintRepository.findById(id).orElseThrow(NotFoundException::new) ;
        Project project = sprintGetProject(sprint.getProject().getId());

        if(dto.getName() != null){ sprint.setName(dto.getName()); }
        if(dto.getDescription() != null){ sprint.setDescription(dto.getDescription());}
        if(dto.getState() != null){ sprint.setState(dto.getState()); }
        if(dto.getDeleted() != null){ sprint.setDeleted(dto.getDeleted()); }
        if(dto.getEndDate() != null){ sprint.setEndDate(dto.getEndDate()); }
        sprintRepository.save(sprint);
        //devuelvo un dto del proyecto con los sprints
        ProjectDTO projectDTO = mapProjectWithSprints(project);
        return projectDTO;
    }





    public Project sprintGetProject(Long id){
        //compruebo que el proyecto exista y que el usuario tenga los permisos adecuados
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.DIRECTOR)){ throw new UnauthorizedAccessException(); }

        return project;
    }


    public ProjectDTO mapProjectWithSprints (Project project){
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.loadFromDomain(project);
        projectDTO.setSprints(findAllSprintsByProject(projectDTO.getId()));
        return projectDTO;
    }

}
