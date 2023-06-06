package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.ProjectBO;
import com.tfg.sprintplannerapi.dto.ProjectDTO;
import com.tfg.sprintplannerapi.dto.ProjectPostDTO;
import com.tfg.sprintplannerapi.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("project")
public class ProjectController {
    @Autowired private ProjectBO projectBO;

    @GetMapping()
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @PageableDefault(page = 0, size = 4) @SortDefault(sort = "updateDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, name = "deleted", defaultValue = "false") Boolean deleted) {
        Page<ProjectDTO> projectsDTO = projectBO.findAllPage(pageable, deleted);
        return ResponseEntity.ok(projectsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getOneProject(@PathVariable Long id) {
        ProjectDTO projectDTO = projectBO.findOneDTO(id);

        return ResponseEntity.ok(projectDTO);
    }
    @GetMapping("/{id}/team")
    public ResponseEntity<TeamDTO> getProjectTeam(@PathVariable Long id) {

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
    public ResponseEntity<?> newProject(@RequestBody ProjectPostDTO dto) {
        ProjectDTO project = projectBO.create(dto);
        return (project != null) ?
                ResponseEntity.ok(project) :
                ResponseEntity.badRequest().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Validated @RequestBody ProjectDTO dto) throws NoSuchMethodException {

            ProjectDTO projectDTO = projectBO.updateProject(id, dto);
            return (projectDTO == null) ?
                    ResponseEntity.badRequest().build() :
                    ResponseEntity.ok(projectDTO);
    }


    @PostMapping("/{id}/participation")
    public ResponseEntity<ProjectDTO> newParticipation(
            @PathVariable Long idProject, //TODO probar si coge el id del proyecto
            @RequestParam(name = "idUser") Long idUser) {
        ProjectDTO project = projectBO.createParticipation(idProject, idUser);
        return ResponseEntity.ok(project);
    }
}
