package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.bo.base.BaseBO;
import com.tfg.sprintplannerapi.dao.ProjectRepository;
import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.User;
import com.tfg.sprintplannerapi.utils.ListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Transactional
public class ProjectBO extends BaseBO <Project, Long, ProjectRepository> {

    @Autowired private UserBO userBO;
    @Autowired private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ProjectDTO> findAllDTO() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //TODO mostrar solo si el usuario registrado participa en los proyectos
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectDTOS = ListMapper.map(projects, ProjectDTO.class);
        return projectDTOS;
    }

    public ProjectDTO findOne(Long id) {
        //TODO mostrar solo si el usuario registrado participa en el proyecto
        Project project = projectRepository.findById(id).orElse(null);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.loadFromDomain(project);
        return projectDTO;
    }

    public Project create(ProjectDTO dto) throws NoSuchMethodException {
        Project project = dto.obtainFromDomain();
        return (project == null)? null : projectRepository.save(project);
    }

    @Override
    public void delete(Project project) {
        User logged = userBO.findUserLogged();
        if(project.getCreatedBy() == logged.getEmail() ){
            project.setDeleted(1);
        }else{
            throw new AccessDeniedException ("Sólo el creador puede eliminar el proyecto.");
        }
    }

    public Project update(Long id, ProjectDTO dto){
        User logged = userBO.findUserLogged();
        Project project = projectRepository.findById(id).orElse(null);
        if(project == null){ return null;}

        if(project.getCreatedBy() == logged.getEmail() ||
                project.getDirector().getEmail() == logged.getEmail()){

            if (dto.getTitle() != null)
                project.setTitle(dto.getTitle());

            if(dto.getDescription() != null)
                project.setDescription(dto.getDescription());

            if(dto.getState() != null)
                project.setState(dto.getState());

            if(dto.getDirector() != null){
                project.setDirector(userRepository.findByEmailIgnoreCase(dto.getDirector()));
            }
        }else{
            throw new AccessDeniedException ("Sólo el director ó creador pueded modificar el proyecto.");
        }
        return project;
    }
}
