package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.dao.ProjectRepository;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.User;
import com.tfg.sprintplannerapi.utils.ListMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectBOTest {


    private ProjectBO projectBO ;
    private ProjectRepository projectRepository;

    private User userLogged;
    @Before
    public void setUp() throws Exception {

        userLogged = new User();
        userLogged.setId(1L);
        userLogged.setEmail("test@gmail.com");

    }

   @Test
    public void testFindAllDTO(){
       /*  // Crear proyectos de ejemplo
        Project project1 = new Project();
        project1.setTitle("Proyecto 1");
        Project project2 = new Project();
        project2.setTitle("Proyecto 2");
        // Agregar los proyectos a la lista de proyectos del usuario
        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);
        userLogged.setProjects(projects);
        // Mockear el comportamiento de la dependencia externa (projectService)
       // when(userLogged.getProjects()).thenReturn(projects);

        // Llamar a la función findAllDTO() del mock
        //List<ProjectDTO> projectDTOS = projectBO.findAllDTO();

        // Verificar que la lista de proyectos DTO no sea nula
        Assertions.assertNotNull(projectDTOS);

        // Verificar que la lista de proyectos DTO tenga la misma cantidad de elementos que la lista de proyectos original
        Assertions.assertEquals(projects.size(), projectDTOS.size());

        // Verificar que los nombres de los proyectos en la lista de proyectos DTO sean los mismos que los nombres de los proyectos originales
        Assertions.assertEquals(project1.getTitle(), projectDTOS.get(0).getTitle());
        Assertions.assertEquals(project2.getTitle(), projectDTOS.get(1).getTitle());

    }*/
    }
}