package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.ProjectBO;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController {
    @Autowired private ProjectBO projectBO;
    @GetMapping("")
    public ResponseEntity<?> getAllProjects() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<ProjectDTO> projectDTOS= projectBO.findAllDTO();
        return (projectDTOS.isEmpty())?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(projectDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneProject(@PathVariable Long id) {
        //TODO permitir acceso sólo a los que sean: del equipo, director ó creador
        ProjectDTO projectDTO= projectBO.findOne(id);
        return (projectDTO == null) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(projectDTO);
    }

    /**
     * Cambia el atributo borrado a true, si la petición la realiza el creador del proyecto.
     * @param id del proyecto
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneProject(@PathVariable Long id) {
        //TODO permitir acceso sólo a los que sean: del equipo, director ó creador
        Project project = projectBO.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        projectBO.delete(project);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    public ResponseEntity<?> newProject(@RequestBody ProjectDTO dto) throws NoSuchMethodException {
        Project project = projectBO.create(dto);
        return (project == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(project);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Validated @RequestBody ProjectDTO dto) throws NoSuchMethodException {
        Project project = projectBO.update(id, dto);
        return (project == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(project);

    }
}
