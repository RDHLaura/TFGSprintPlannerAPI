package com.tfg.sprintplannerapi.controller;


import com.tfg.sprintplannerapi.bo.ProjectBO;
import com.tfg.sprintplannerapi.dto.*;
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
@RequestMapping("/project")
public class ProjectController {
    @Autowired private ProjectBO projectBO;

    @GetMapping()
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @PageableDefault(page = 0, size = 4) @SortDefault(sort = "updateDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, name = "deleted", defaultValue = "false") Boolean deleted) {
        Page<ProjectDTO> projectsDTO = projectBO.findAllPage(pageable, deleted);
        return ResponseEntity.ok(projectsDTO);
    }

    @PostMapping()
    public ResponseEntity<?> newProject(@RequestBody ProjectPostDTO dto) {
        ProjectDTO project = projectBO.create(dto);
        return (project != null) ?
                ResponseEntity.ok(project) :
                ResponseEntity.badRequest().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getOneProject(@PathVariable Long id) {
        ProjectDTO projectDTO = projectBO.findOneDTO(id);
        //incluye los sprints en el dto de proyecto
        projectDTO.setSprints(projectBO.findAllSprintsByProject(projectDTO.getId()));
        return ResponseEntity.ok(projectDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Validated @RequestBody ProjectDTO dto) {

        ProjectDTO projectDTO = projectBO.updateProject(id, dto);
        return (projectDTO == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(projectDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneProject(@PathVariable Long id) {
        projectBO.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sprint")
    public ResponseEntity<ProjectDTO> newSprint( @RequestBody SprintPostDTO sprintDTO) {
        ProjectDTO updateProject = projectBO.createSprint(sprintDTO);
        return ResponseEntity.ok(updateProject);
    }

    @PutMapping("/sprint/{id}")
    public ResponseEntity<?> updateSprint(@PathVariable Long id, @RequestBody SprintPostDTO dto) {

        ProjectDTO projectDTO = projectBO.updateSprint( id,  dto);
        return (projectDTO == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(projectDTO);
    }






}
