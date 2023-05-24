package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.ProjectRepository;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.dto.TeamDTO;
import com.tfg.sprintplannerapi.error.BadInputException;
import com.tfg.sprintplannerapi.error.MappingException;
import com.tfg.sprintplannerapi.error.NotFoundException;
import com.tfg.sprintplannerapi.error.UnauthorizedAccessException;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.Rol;
import com.tfg.sprintplannerapi.model.User;
import com.tfg.sprintplannerapi.utils.ListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Transactional
public class ProjectBO extends BaseBO <Project, Long, ProjectDTO, ProjectRepository> {

    @Autowired private UserBO userBO;
    @Autowired private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    private User userLogged;
    public List<ProjectDTO> findAllDTO() {
        userLogged = userBO.findUserLogged() ;
        //Lista los proyectos en los que participa el usuario registrado
        List<Project> projects = userLogged.getProjects();

        try {
            return ListMapper.map(projects, ProjectDTO.class);
        } catch (NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException e) {
            throw new MappingException();
        }
    }

    public TeamDTO findTeam(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.PARTICIPANTE) ){
            throw new UnauthorizedAccessException();
        }
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.loadFromDomain(project);
        return teamDTO;
    }

    public ProjectDTO create(ProjectDTO dto) {
        userLogged = userBO.findUserLogged() ;

        dto.setId(null); //se setea a null por si viene en la request con un id no pise ninguna entidad ya creada
        Project project = null;
        try {
            project = dto.obtainFromDomain();
        } catch (NoSuchMethodException e) {
            throw new MappingException();
        }
        if(dto.getDirectorEmail() != null){
            project.setDirector(userRepository.findByEmailIgnoreCase(dto.getDirectorEmail()).orElse(null));
        }
        //añado el creador al team y al director si es distinto del creador
        project.getTeam().add(userLogged);
        if (project.getDirector() != userLogged){
            project.getTeam().add(userLogged);
        }
        Project created = projectRepository.save(project);
        ProjectDTO createdDTO = new ProjectDTO();
        createdDTO.loadFromDomain(created);
        return createdDTO;
    }


    public Project deleteProject(Long id)  {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.CREADOR) ){
            throw new UnauthorizedAccessException();
        }
        project.setDeleted(true);
        return projectRepository.save(project);
    }

    public ProjectDTO updateProject(Long id, ProjectDTO dto)  {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.CREADOR) && !roles.contains(Rol.DIRECTOR) ){
            throw new UnauthorizedAccessException();
        }
        if (dto.getTitle() != null)
            project.setTitle(dto.getTitle());

        if(dto.getDescription() != null)
            project.setDescription(dto.getDescription());

        if(dto.getState() != null)
            project.setState(dto.getState());

        if(dto.getDirectorEmail() != null){
            User newDirector = userRepository.findByEmailIgnoreCase(dto.getDirectorEmail()).orElse(null);
            project.setDirector(newDirector);
            //si el nuevo director no está ya en el team se incorpora
            if( !project.getTeam().contains(newDirector)){
                project.getTeam().add(newDirector);
            }
        }
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.loadFromDomain(project);
        return projectDTO;
    }


    public Project createParticipation(Long idProject, Long idUser ) throws ChangeSetPersister.NotFoundException {
        userLogged = userBO.findUserLogged();
        Project project = projectRepository.findById(idProject).orElseThrow(ChangeSetPersister.NotFoundException::new);
        User user = userRepository.findById(idUser).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.CREADOR) && !roles.contains(Rol.DIRECTOR) ){ //si no tiene permisos
            throw new UnauthorizedAccessException();
        }
        if(project.getTeam().contains(user)){ //si ya existe la participación
            throw new BadInputException();
        }
        project.getTeam().add(user);
        return projectRepository.save(project);

    }

}
