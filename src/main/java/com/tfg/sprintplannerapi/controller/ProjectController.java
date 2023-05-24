package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.ProjectBO;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController {
    @Autowired private ProjectBO projectBO;


    @GetMapping()
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projectsDTO = projectBO.findAllDTO();
        return (projectsDTO.isEmpty())?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(projectsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getOneProject(@PathVariable Long id) {
        ProjectDTO projectDTO = projectBO.findOneDTO(id);

        return ResponseEntity.ok(projectDTO);
    }
    @GetMapping("/{id}/team")
    public ResponseEntity<TeamDTO> getOneProjectTeam(@PathVariable Long id) {

        TeamDTO teamDTO= projectBO.findTeam(id);
        return ResponseEntity.ok(teamDTO);
    }

    /**
     * Cambia el atributo borrado a true, si la petición la realiza el creador del proyecto.
     * @param id del proyecto
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneProject(@PathVariable Long id) {
        projectBO.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<ProjectDTO> newProject(@RequestBody ProjectDTO dto) {
        ProjectDTO project = projectBO.create(dto);
        return ResponseEntity.ok(project);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Validated @RequestBody ProjectDTO dto) throws NoSuchMethodException {

            ProjectDTO projectDTO = projectBO.updateProject(id, dto);
            return (projectDTO == null) ?
                    ResponseEntity.badRequest().build() :
                    ResponseEntity.ok(projectDTO);
    }
}
