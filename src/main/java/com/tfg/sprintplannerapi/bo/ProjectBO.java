package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.ProjectRepository;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.dto.ProjectPostDTO;
import com.tfg.sprintplannerapi.dto.TeamDTO;
import com.tfg.sprintplannerapi.error.*;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.Rol;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectBO extends BaseBO <Project, Long, ProjectDTO, ProjectRepository> {

    @Autowired private UserBO userBO;
    @Autowired private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    private User userLogged;

    public Page<ProjectDTO> findAllPage(Pageable pageable, Boolean deleted) {
        userLogged = userBO.findUserLogged();
        Page<Project> projects = projectRepository.findAllByDeletedAndTeamContaining(deleted, userLogged, pageable);
        if (userLogged.getProjects().isEmpty()) {
            throw new NoContentException();
        }
        /**CONVIERTO A DTO*/
        List<ProjectDTO> projectsDTO ;
        try {
            projectsDTO = ListMapper.map(projects.getContent(), ProjectDTO.class);
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new MappingException();
        }
        return new PageImpl<>(projectsDTO, pageable, projects.getContent().size());
    }
    public Page<ProjectDTO> findAll(Pageable pageable, Boolean deleted) {
        userLogged = userBO.findUserLogged() ;

        if(userLogged.getProjects().isEmpty()){
            throw new NoContentException();
        }
        /**INICIALIZACIÓN ENTIDADES -> LAZY*/
        userLogged.getProjects().forEach(project -> {
            Hibernate.initialize(project);
        });

        /**Obtengo los proyectos activos o eliminados según la request*/
        List<Project> projects;
        if(deleted){
            projects  = userLogged.getProjects().stream().filter(p -> p.getDeleted()).collect(Collectors.toList());
        }else{
            projects  = userLogged.getProjects().stream().filter(p -> !p.getDeleted()).collect(Collectors.toList());
        }
        /**PAGINACIÓN*/
        //ordeno los proyectos por fecha de modificación
        Collections.sort(projects, (u1, u2) -> u2.getUpdateDate().compareTo(u1.getUpdateDate()));
        //obtengo los elementos paginados
        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), projects.size());
        List<Project> sublist  = projects.subList(startIndex, endIndex);
        /**CONVIERTO A DTO*/
        List<ProjectDTO> projectsDTO ;
        try {
            projectsDTO = ListMapper.map(sublist, ProjectDTO.class);
        } catch (NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException e) {
            throw new MappingException();
        }
        return new PageImpl<>(projectsDTO, pageable, projects.size());
    }



    /**No lo estoy usando, obtengo el equipo del proyecto*/
    public TeamDTO findTeam(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        Hibernate.initialize(project);
        //comprueba si el usuario logueado tiene acceso a ese proyecto
        userLogged = userBO.findUserLogged();
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.PARTICIPANTE) ){
            throw new UnauthorizedAccessException();
        }

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.loadFromDomain(project);
        return teamDTO;
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
            project.setDirector(userRepository.findByEmailIgnoreCase(dto.getDirectorEmail()).orElse(null));
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
        ProjectDTO createdDTO = new ProjectDTO();
        createdDTO.loadFromDomain(created);
        return createdDTO;
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
        //TODO actualizar el team
        //comprobar que el team del dto es distinto del team entidad
            //sí-> comprobar que el creador o director estén en el team
                //no-> enviar badRequest (mensaje: no puede eliminar al creador /para eliminar al director del equipo deberá
                            //asignar la dirección a otro miembro del equipo primero
                //sí-> se actualiza el team
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

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.loadFromDomain(project);
        return projectDTO;
    }


    /**No lo estoy usando -> ProjectUpdate*/
    public ProjectDTO createParticipation(Long idProject, Long idUser )  {
        userLogged = userBO.findUserLogged();
        Project project = projectRepository.findById(idProject).orElseThrow(()-> new NotFoundException("There is no project with ID: " + idProject));
        User user = userRepository.findById(idUser).orElseThrow(()-> new NotFoundException("There is no project with ID: " + idUser));
        //compruebo si el usuario logueado tiene permisos para incluir un nuevo participante en el proyecto
        List<Rol> roles = Rol.getRolUser(userLogged, project);
        if(!roles.contains(Rol.CREADOR) && !roles.contains(Rol.DIRECTOR) ){ //si no tiene permisos
            throw new UnauthorizedAccessException();
        }
        Hibernate.initialize(project.getTeam());
        if(project.getTeam().contains(user)){ //si ya existe la participación
            throw new BadInputException("The user is already participating in that project.");
        }
        project.getTeam().add(user);
        ProjectDTO updateProject = new ProjectDTO();
        updateProject.loadFromDomain(projectRepository.save(project));
        return updateProject;
    }

}
